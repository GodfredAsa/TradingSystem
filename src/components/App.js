import React, {useState} from 'react';
import '../styling/App.css';
import '../styling/Header.css';

import SignUp from './SignUp';
import SignIn from './SignIn';

const App = () => {
  const [page, setPage] = useState(true);

  const setSignIn = () => setPage(true);
  const setSignUp = () => setPage(false);

  return (
    <div>
      <div className='header-main'>
            <ul className='header-items'>
                <li ><span className='logo'>KARYDE</span></li>
                <li><button onClick={setSignIn}>LOGIN</button></li>
                <li><button onClick={setSignUp}>REGISTER</button></li>
            </ul>
        </div>
      {
        page? <SignIn/> : <SignUp/>
      }
    </div>
  );
}

export default App;
