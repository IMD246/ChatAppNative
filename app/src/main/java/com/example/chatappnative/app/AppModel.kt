import com.example.chatappnative.presentation.auth.data.domain.entity.UserInfoEntity

object AppModel {
    var userInfo: UserInfoEntity? = null

    fun updateUserInfo(value: UserInfoEntity?) {
        userInfo = value
    }

    fun clearUserInfo() {
        userInfo = null
    }
}