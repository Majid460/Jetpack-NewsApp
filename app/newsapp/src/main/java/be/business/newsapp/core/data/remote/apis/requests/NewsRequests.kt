package be.business.newsapp.core.data.remote.apis.requests

import io.ktor.resources.Resource

@Resource("/v2/top-headlines")
data class NewsResource(
    val country: String? = null,
    val sources: String? = null,
    val category: String? = null,
)