import dayjs from 'dayjs';
import { IClient } from 'app/shared/model/client.model';
import { IAgent } from 'app/shared/model/agent.model';
import { TicketStatus } from 'app/shared/model/enumerations/ticket-status.model';

export interface ITicket {
  id?: number;
  code?: string;
  status?: TicketStatus;
  issueDescription?: string;
  issuedAt?: string;
  issuedBy?: IClient | null;
  assignedTo?: IAgent | null;
}

export const defaultValue: Readonly<ITicket> = {};
