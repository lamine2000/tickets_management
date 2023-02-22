import dayjs from 'dayjs';
import { ITicket } from 'app/shared/model/ticket.model';
import { IUser } from 'app/shared/model/user.model';

export interface IMessage {
  id?: number;
  content?: string;
  sentAt?: string;
  ticket?: ITicket | null;
  sentBy?: IUser | null;
}

export const defaultValue: Readonly<IMessage> = {};
