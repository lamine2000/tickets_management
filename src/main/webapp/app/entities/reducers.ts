import ticket from 'app/entities/ticket/ticket.reducer';
import client from 'app/entities/client/client.reducer';
import agent from 'app/entities/agent/agent.reducer';
import message from 'app/entities/message/message.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  ticket,
  client,
  agent,
  message,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
