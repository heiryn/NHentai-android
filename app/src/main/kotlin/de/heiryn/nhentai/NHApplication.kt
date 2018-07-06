package de.heiryn.nhentai

import android.app.Application
import com.orhanobut.hawk.Hawk
import com.orhanobut.hawk.Parser
import de.heiryn.nhentai.util.GsonUtils
import java.lang.reflect.Type

class NHApplication: Application() {

	override fun onCreate() {
		super.onCreate()
		Hawk.init(this).setParser(object: Parser {
			override fun <T : Any?> fromJson(content: String?, type: Type?): T
					= GsonUtils.fromJson(content!!, type!!)
			override fun toJson(body: Any?): String = GsonUtils.toJson(body)
		}).build()
	}

}