import { IUser } from 'app/shared/model/user.model';

export interface IAgent {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  user?: IUser | null;
}

export const defaultValue: Readonly<IAgent> = {};
