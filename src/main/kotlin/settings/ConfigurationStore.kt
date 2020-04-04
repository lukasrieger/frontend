package settings

import arrow.core.Option

enum class ConfigKey {
    SOURCES_PATH,
    LOG_FILE_PATH,
    DATABASE_PATH
}

interface ConfigurationStore {
    operator fun get(key: ConfigKey): Option<String>
    operator fun set(key: ConfigKey, value: String)

}


class FileConfigurationStore : ConfigurationStore {
    override fun get(key: ConfigKey): Option<String> = TODO()
    override fun set(key: ConfigKey, value: String) = TODO()
}
