package hr.kacan.trznica.view.login

/**
 * Data validation state of the login form.
 */
class LoginFormState ( var usernameError: Int, var passwordError: Int, var isDataValid: Boolean){

    internal constructor(usernameError: Int, passwordError: Int) : this(usernameError, passwordError, false)

    internal constructor(isDataValid: Boolean) : this(0, 0, isDataValid)
}