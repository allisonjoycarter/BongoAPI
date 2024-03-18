package com.catscoffeeandkitchen.features.poe

import com.catscoffeeandkitchen.features.poe.models.ninja.CurrencyOverview
import com.catscoffeeandkitchen.features.poe.models.trade.TradeItemID
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ExpiryPolicyBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import org.ehcache.config.units.EntryUnit
import org.ehcache.config.units.MemoryUnit
import org.ehcache.impl.config.persistence.CacheManagerPersistenceConfiguration
import java.io.File
import java.time.Duration
import java.time.temporal.ChronoUnit

class PoeBaseDataCache(
    private val delegate: PoeBaseData,
    storagePath: File
): PoeBaseData {
    private val cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
        .with(CacheManagerPersistenceConfiguration(storagePath))
        .withCache(
            "poeBaseDataCache",
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                String::class.javaObjectType,
                String::class.javaObjectType,
                ResourcePoolsBuilder.newResourcePoolsBuilder()
                    .heap(MAX_ENTRIES, EntryUnit.ENTRIES)
                    .offheap(MAX_HEAP_MB, MemoryUnit.MB)
                    .disk(MAX_STORAGE_MB, MemoryUnit.MB, true)
                    .build()
            ).withExpiry(
                ExpiryPolicyBuilder.timeToLiveExpiration(Duration.of(DAYS_TO_CACHE, ChronoUnit.DAYS))
            )
        )
        .build(true)

    private val cacheInstance = cacheManager.getCache(
        "poeBaseDataCache",
        String::class.javaObjectType,
        String::class.javaObjectType
    )

    override suspend fun getCurrentLeagueName(): String {
        return cacheInstance["currentLeague"]
            ?: delegate.getCurrentLeagueName().also { league ->
                cacheInstance.put("currentLeague", league)
            }
    }

    override suspend fun getTradeData(): List<TradeItemID> {
        val tradeData = cacheInstance["tradeData"]
        if (tradeData != null) {
            return Json.decodeFromString(tradeData)
        }

        return delegate.getTradeData().also { items ->
            cacheInstance.put("tradeData", Json.encodeToString(items))
        }
    }

    override suspend fun getCurrencyInfo(): CurrencyOverview {
        val currencyOverview = cacheInstance["currencyOverview"]
        if (currencyOverview != null) {
            return Json.decodeFromString(currencyOverview)
        }

        return delegate.getCurrencyInfo().also { overview ->
            cacheInstance.put("currencyOverview", Json.encodeToString(overview))
        }
    }

    companion object {
        const val MAX_ENTRIES = 1000L
        const val MAX_HEAP_MB = 10L
        const val MAX_STORAGE_MB = 100L
        const val DAYS_TO_CACHE = 1L
    }
}
