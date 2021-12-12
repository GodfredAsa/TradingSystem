import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  form!: FormGroup

  constructor(private formBuilder: FormBuilder, private http: HttpClient, private router: Router) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      firstName: "",
      lastName: "",
      password: "",
      email: "",
    })
  }

  registerRegulator(){
    this.http.post("http://localhost:8080/api/client/auth/signup/regulator", this.form.getRawValue())
    .subscribe(
        {
          next: res => {console.log(res);
            this.router.navigate(["/login"])
          }
        }
       )
  }

}
