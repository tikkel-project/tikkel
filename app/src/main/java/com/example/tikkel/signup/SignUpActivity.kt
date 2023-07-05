package com.example.tikkel.signup

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.tikkel.MainActivity
import com.example.tikkel.databinding.ActivitySignUpBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause.*
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User

class SignUpActivity : AppCompatActivity() {
    private var _binding: ActivitySignUpBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.kakaoLoginButton.setOnClickListener {
            kakaoLogin()
        }
    }

    // 이메일 로그인 콜백
    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            setLogin(false)
            when {
                error.toString() == AccessDenied.toString() -> {
                    Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                }
                error.toString() == InvalidClient.toString() -> {
                    Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                }
                error.toString() == InvalidGrant.toString() -> {
                    Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT)
                        .show()
                }
                error.toString() == InvalidRequest.toString() -> {
                    Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                }
                error.toString() == InvalidScope.toString() -> {
                    Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                }
                error.toString() == Misconfigured.toString() -> {
                    Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT)
                        .show()
                }
                error.toString() == ServerError.toString() -> {
                    Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                }
                error.toString() == Unauthorized.toString() -> {
                    Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                }
                else -> { // Unknown
                    Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (token != null) {
            setLogin(true)
            Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
            Log.d("access",  "${token.accessToken}")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            //getkakao()

        }
    }

    private fun kakaoLogin() {

        if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)) { // 카카오톡 설치O
            UserApiClient.instance.loginWithKakaoTalk(this){ token, error ->
                if (error != null) {
                    TestMsg(this, "kakaoLogin 실패 : ${error}")
                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                }
                else if (token != null) {
                    TestMsg(this, "kakaoLogin 성공 ${token.accessToken}")
                    setLogin(true)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        else{ // 설치 안된 경우
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private fun kakaoLogout(){
        // 로그아웃
        UserApiClient.instance.logout { error ->
            if (error != null) {
                TestMsg(this, "로그아웃 실패. SDK에서 토큰 삭제됨: ${error}")
            }
            else {
                TestMsg(this, "로그아웃 성공. SDK에서 토큰 삭제됨")
                setLogin(false)
            }
        }
    }

    private fun kakaoUnlink(){
        // 연결 끊기
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                TestMsg(this, "연결 끊기 실패: ${error}")
            }
            else {
                TestMsg(this, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                setLogin(false)
            }
        }
    }

    private fun TestMsg(act: Activity, msg : String){
        binding.signupLogo.text = msg
    }

    private fun setLogin(bool: Boolean){
        binding.kakaoLoginButton.visibility = if(bool) View.GONE else View.VISIBLE
        //binding.kakaoLogoutButton.visibility = if(bool) View.VISIBLE else View.GONE
        //binding.kakaoUnlinkButton.visibility = if(bool) View.VISIBLE else View.GONE
    }
// 추후 sqllite같은 앱 내부 db에 데이터를 저장해야함.
    private fun getkakao(){
        UserApiClient.instance.me { user, error ->
            Log.d("kakao","실행")
            if (user!=null){
                binding.signupLogo.text = user.kakaoAccount?.profile?.nickname
            }
            else{
                binding.signupLogo.text = null
            }
        }
    }

}




