package hr.kacan.trznica.view.login

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult(var success: LoggedInUserView, var error: Int)