package com.example.ruok_workers

import android.os.Parcel
import android.os.Parcelable

data class ConsultationItem(
    var m_num: Int, //회원번호
    var h_num: Int,//노숙인번호
    var c_time: String, //작성시간
    var health: Int, //건강상태코드
    var unusual: String, //특이사항
    var measure: String, //조치사항
    var content: String, //상담내용
    var addr: String, //주소
    var latitude: Double, //위도
    var longitude: Double, //경도
    var filename: Array<String>//사진파일명
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.createStringArray() ?: arrayOf("")
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(m_num)
        parcel.writeInt(h_num)
        parcel.writeString(c_time)
        parcel.writeInt(health)
        parcel.writeString(unusual)
        parcel.writeString(measure)
        parcel.writeString(content)
        parcel.writeString(addr)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeStringArray(filename)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ConsultationItem> {
        override fun createFromParcel(parcel: Parcel): ConsultationItem {
            return ConsultationItem(parcel)
        }

        override fun newArray(size: Int): Array<ConsultationItem?> {
            return arrayOfNulls(size)
        }
    }

}
