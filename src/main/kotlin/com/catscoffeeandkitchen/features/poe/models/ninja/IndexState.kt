package com.catscoffeeandkitchen.features.poe.models.ninja

import kotlinx.serialization.Serializable

@Serializable
data class IndexState(
    val economyLeagues: List<EconomyLeague>,
    val oldEconomyLeagues: List<EconomyLeague>
//    val snapshotVersions: List<Snapshot>,
//    val buildLeagues: List<BuildLeague>,
//    val oldBuildLeagues: List<BuildLeague>
)


