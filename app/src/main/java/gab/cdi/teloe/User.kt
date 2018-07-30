package gab.cdi.teloe

data class User(var fullname: String ="",var username: String = "", var email: String = "",var phoneNumber: String = "", var userSettings: Settings = Settings(0,0)) {

}