import dayjs from 'dayjs';
import { IClient } from 'app/shared/model/client.model';
import { TicketStatus } from 'app/shared/model/enumerations/ticket-status.model';

export interface ITicket {
  id?: number;
  code?: string;
  status?: TicketStatus;
  issueDescription?: string;
  issuedAt?: string;
  issuedBy?: IClient | null;
  assignedTo?: IClient | null;
}

export const defaultValue: Readonly<ITicket> = {};
