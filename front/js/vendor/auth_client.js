function goToRegistrationCode(){
  window.location.href="../../html/auth_client/registration_code.html";
}

function goToRegisterLabel(){
  window.location.href = "../../html/auth_client/registration_label.html";
}

function goToRegisterUserName() {
  window.location.href = "../../html/auth_client/register_user_name.html";
}

function goToHomePage() {
  window.location.replace("../../html/home_client/home_page.html");
}

function goToRecoveryCode(){
  window.location.href = "../../html/auth_client/recovery_code.html";
}

function goToNewPassword(){
  window.location.href = "../../html/auth_client/new_password.html";
}

async function sendLoginAndPassword(log, password) {
  try {
    const response = await fetch('http://localhost:8081/api/auth/login', {
      method: "POST",
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        login: log,
        password: password
      })
    });
    if (response.ok) {
      console.log('✅ Login successful!');
      goToHomePage();
    } else {
      const errorData = await response.json();
      console.error('❌ Login failed:', errorData.message || 'Unknown error');
      alert('Login failed: ' + (errorData.message || 'Invalid credentials'));
    }
  } catch (error) {
    console.error('❌ Connection error:', error);
    alert('Connection error. Please check if server is running.');
  }
}
