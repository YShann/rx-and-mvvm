package tw.edu.ntub.imd.birc.rxandmvvm.dao

class UserDao {
//    fun login(name: String?, password: String?): Boolean {
//        val sql = "select * from users where name = ? and password = ?"
//        val con: Connection = JDBCUtils.getConn()
//        try {
//            val pst = con.prepareStatement(sql)
//            pst.setString(1, name)
//            pst.setString(2, password)
//            if (pst.executeQuery().next()) {
//                return true
//            }
//        } catch (throwables: SQLException) {
//            throwables.printStackTrace()
//        } finally {
//            JDBCUtils.close(con)
//        }
//        return false
//    }
//
//    fun register(user: User): Boolean {
//        val sql = "insert into users(name,username,password,age,phone) values (?,?,?,?,?)"
//        val con: Connection = JDBCUtils.getConn()
//        try {
//            val pst = con.prepareStatement(sql)
//            pst.setString(1, user.getName())
//            pst.setString(2, user.getUsername())
//            pst.setString(3, user.getPassword())
//            pst.setInt(4, user.getAge())
//            pst.setString(5, user.getPhone())
//            val value = pst.executeUpdate()
//            if (value > 0) {
//                return true
//            }
//        } catch (throwables: SQLException) {
//            throwables.printStackTrace()
//        } finally {
//            JDBCUtils.close(con)
//        }
//        return false
//    }
//
//    fun findUser(name: String?): User? {
//        val sql = "select * from users where name = ?"
//        val con: Connection = JDBCUtils.getConn()
//        var user: User? = null
//        try {
//            val pst = con.prepareStatement(sql)
//            pst.setString(1, name)
//            val rs = pst.executeQuery()
//            while (rs.next()) {
//                val id = rs.getInt(0)
//                val namedb = rs.getString(1)
//                val username = rs.getString(2)
//                val passworddb = rs.getString(3)
//                val age = rs.getInt(4)
//                val phone = rs.getString(5)
//                user = User(id, namedb, username, passworddb, age, phone)
//            }
//        } catch (throwables: SQLException) {
//            throwables.printStackTrace()
//        } finally {
//            JDBCUtils.close(con)
//        }
//        return user
//    }
}