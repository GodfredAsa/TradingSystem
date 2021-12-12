import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form!: FormGroup
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
    this.http.post("http://localhost:8080/api/client/auth/signin", this.form.getRawValue, {withCredentials: true}).
    subscribe({next: () => this.router.navigate(["/"])})
  }
}
