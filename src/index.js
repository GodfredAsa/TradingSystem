import React from 'react';
import ReactDOM from 'react-dom';
import App from './components/App';
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import {ComponentRoutes} from './utilities/routes'

ReactDOM.render(
  <React.StrictMode>
    <Router>
      <Routes>
        <Route exact path="/" element={<App />} />
        {ComponentRoutes.map(route => {
          return <Route exact={route.exact} path={route.path} element={<route.element />} />
        })}
      </Routes>
    </Router>
  </React.StrictMode>,
  document.getElementById('root')
);


