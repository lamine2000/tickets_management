import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Ticket from './ticket';
import TicketDetail from './ticket-detail';
import TicketUpdate from './ticket-update';
import TicketDeleteDialog from './ticket-delete-dialog';

const TicketRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Ticket />} />
    <Route path="new" element={<TicketUpdate />} />
    <Route path=":id">
      <Route index element={<TicketDetail />} />
      <Route path="edit" element={<TicketUpdate />} />
      <Route path="delete" element={<TicketDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TicketRoutes;
