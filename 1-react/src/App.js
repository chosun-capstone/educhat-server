import React from 'react';


function App() {
  return (
<div className="login-container">
  <div className="login-box">
    <div className="white-text">EDUCHAT에 오신것을</div>
    <div className="white-text">환영합니다</div>
  </div>
  <div className="login-box1">
    
    <h4 >다음으로 로그인하기</h4>
    <div className="login-options">
      <button className="kakao-login">
        <img src='./images/kakaologin.png' alt="KaKao로 로그인" />
      </button>
      <button className="google-login">
  <img src='./images/googlelogin.png' alt="Google로 로그인하기" />
  
</button>
    </div>
    <div className="copyright">
      로그인 할 경우 <span className="privacy-policy">개인정보방침</span>에 동의합니다
    </div>
  </div>
</div>


  );
}

export default App;