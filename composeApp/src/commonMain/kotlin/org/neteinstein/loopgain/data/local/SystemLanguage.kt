package org.neteinstein.loopgain.data.local

import org.neteinstein.loopgain.domain.model.Language

/**
 * The device's current system language, mapped to a supported [Language] — or null if the system
 * language isn't one LoopGain offers. Used only to seed the very first launch's default; once the
 * user has picked a language in Settings, that saved preference always wins.
 */
expect fun platformSystemLanguage(): Language?
