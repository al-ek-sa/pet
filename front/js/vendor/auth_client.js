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

const http = new XMLHttpRequest();
const url = 'cvbnm,';
http.open("GET", url);
http.send();

http.onreadystatechange = (e) => {
  console.log(http.responseText);
}
