import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Ticket from './ticket';
import Client from './client';
import Agent from './agent';
import Message from './message';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="ticket/*" element={<Ticket />} />
        <Route path="client/*" element={<Client />} />
        <Route path="agent/*" element={<Agent />} />
        <Route path="message/*" element={<Message />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
