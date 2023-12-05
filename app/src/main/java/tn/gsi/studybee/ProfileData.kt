package tn.gsi.studybee

class ProfileData {
  var dataName : String? =null
  var dataEstab : String? =null
  var dataField : String? =null
  var dataContact : String? =null
  var dataImage : String? =null

    constructor(dataName: String?,dataEstab: String?,dataField: String?,dataContact: String?,dataImage: String? ){
        this.dataName = dataName
        this.dataEstab = dataEstab
        this.dataField = dataField
        this.dataContact = dataContact
        this.dataImage = dataImage
    }
}