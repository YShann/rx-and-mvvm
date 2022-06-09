package tw.edu.ntub.imd.birc.rxandmvvm.entity

class User {
    private var id = 0
    private var name: String? = null
    private var username: String? = null
    private var password: String? = null
    private var age = 0
    private var phone: String? = null




    fun User(
        id: Int,
        name: String?,
        username: String?,
        password: String?,
        age: Int,
        phone: String?
    ) {
        this.id = id
        this.name = name
        this.username = username
        this.password = password
        this.age = age
        this.phone = phone
    }

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getUsername(): String? {
        return username
    }

    fun setUsername(username: String?) {
        this.username = username
    }

    fun getPassword(): String? {
        return password
    }

    fun setPassword(password: String?) {
        this.password = password
    }

    fun getAge(): Int {
        return age
    }

    fun setAge(age: Int) {
        this.age = age
    }

    fun getPhone(): String? {
        return phone
    }

    fun setPhone(phone: String?) {
        this.phone = phone
    }
}