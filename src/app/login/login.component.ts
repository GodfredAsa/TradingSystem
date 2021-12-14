import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginRequest } from '../models/login-request';
import { loginResponse } from '../models/login-response';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form!: FormGroup;

  loginRequestPayload:LoginRequest = new LoginRequest;
  loginResponse: loginResponse;

  constructor(
    private formBuilder: FormBuilder, 
    private http: HttpClient,
    private router: Router) {}


  ngOnInit(): void {
    this.form = this.formBuilder.group({
      email: "",
      password: ""
    })
  }

  logIn(): void {

    this.loginRequestPayload.email = this.form.controls['email'].value;
    this.loginRequestPayload.password = this.form.controls['password'].value;

    console.log(this.loginRequestPayload);

  //   this.http.post("http://localhost:8080/api/client/auth/signin", this.loginRequestPayload,{responseType:'json'}).
  //   subscribe( ()
  //     {next: () => this.router.navigate(["/"]),
  //     error: err => {
  //     alert("Wrong Credentials!!")
  //     this.form.reset
  //     }
  // })

  this.http.post<loginResponse>("http://localhost:8080/api/client/auth/signin", this.loginRequestPayload,{responseType:'json'}).subscribe((response) => {
  },(error) =>{
      if(error.status == 302){
        this.loginResponse = error.message;
        console.log(this.loginResponse);
        this.router.navigate(["/"]);
      }
  });

  }
}
