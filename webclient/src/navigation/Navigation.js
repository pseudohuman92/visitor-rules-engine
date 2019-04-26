import React from 'react';
import { Link } from 'react-router-dom';

import * as ROUTES from '../constants/Routes';

const Navigation = () => (
  <div>
    <ul>
      <li>
        <Link to={ROUTES.GAME}>Game</Link>
      </li>
      <li>
        <Link to={ROUTES.ACCOUNT}>Account</Link>
      </li>
      <li>
        <Link to={ROUTES.DECK_BUILDER}>Deck Builder</Link>
      </li>
    </ul>
  </div>
);

export default Navigation;