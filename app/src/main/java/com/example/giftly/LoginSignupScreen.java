package com.example.giftly;

public class LoginSignupScreen {
    private SignUpInterface sign = new signinScreen();
    private SignUpInterface noSign = new no_signinScreen();
    private SignUpInterface currentState = noSign;
    private SignUpInterface txtLogin = new signinScreen();
    private SignUpInterface btnSubmitting = new signinScreen();

    public void signPress() {
        if(currentState == sign){
            this.currentState.handleLogin();

        }
        else{
            this.txtLogin.textLoginInfo();
            this.currentState.handleSignUp();

        }
    }

    public void buttonPress() {
        this.btnSubmitting.buttonSubmit();
    }

}