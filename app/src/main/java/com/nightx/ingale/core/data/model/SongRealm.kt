package com.nightx.ingale.core.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class SongRealm : RealmObject {
    @PrimaryKey
    var id:             Long =   0L
    var title:          String = ""
    var album:          String = ""
    var duration:       Long =    0
    var artist:         String = ""
    var genre:          String = ""
    var path:           String = ""
    var previewPath:    String = ""
    var artistId:       Long =   0L
    var albumId:        Long =   0L
    var lastModified:   Long =   0L
}
