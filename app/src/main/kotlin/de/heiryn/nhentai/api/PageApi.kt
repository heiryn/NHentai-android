package de.heiryn.nhentai.api

import kotlinx.coroutines.experimental.*
import de.heiryn.nhentai.model.PageResult
import de.heiryn.nhentai.util.HttpUtils
import de.heiryn.nhentai.util.extension.readAsJsonObject

object PageApi {

	suspend fun getPageList(url: String): PageResult? = HttpUtils.requestUrl(url)
			?.readAsJsonObject()

	suspend fun getHomePageList(pageNum: Int): PageResult? = ApiConstants.getHomePageUrl(pageNum)
			.run(HttpUtils::requestUrl)
			?.readAsJsonObject()

	suspend fun getSearchPageList(keyword: String, pageNum: Int): PageResult? = ApiConstants
			.getSearchUrl(keyword, pageNum)
			.run(HttpUtils::requestUrl)
			?.readAsJsonObject()

	fun getPageListAsync(url: String): Deferred<PageResult?>
			= async(CommonPool) { getPageList(url) }

	fun getHomePageListAsync(pageNum: Int): Deferred<PageResult?>
			= async(CommonPool) { getHomePageList(pageNum) }

	fun getSearchPageListAsync(keyword: String, pageNum: Int): Deferred<PageResult?>
			= async(CommonPool) { getSearchPageList(keyword, pageNum) }

}