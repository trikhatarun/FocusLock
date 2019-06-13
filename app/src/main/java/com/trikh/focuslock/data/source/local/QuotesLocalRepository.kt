package com.trikh.focuslock.data.source.local

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trikh.focuslock.Application
import com.trikh.focuslock.data.model.Quote
import java.io.IOException
import java.nio.charset.StandardCharsets


class QuotesLocalRepository() {
    private var quotes: List<Quote>

    init {
        val quotesJson = getAssetJsonData("quotes.json")
        val listType = object : TypeToken<ArrayList<Quote>>() {}.type

        quotes = Gson().fromJson(quotesJson, listType)
        quotes.forEach {
            println(it.quote)
        }
    }

    fun getQuote(): Quote = quotes.random()

    private fun getAssetJsonData(fileName: String): String? {
        var json: String? = null
        try {
            val inputStream = Application.instance.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    companion object {
        @Volatile
        private var instance: QuotesLocalRepository? = null
        private val Lock = Any()

        fun getInstance() = instance ?: synchronized(Lock) {
            instance ?: QuotesLocalRepository()
        }
    }
}