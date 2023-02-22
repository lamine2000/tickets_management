import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITicket } from 'app/shared/model/ticket.model';
import { getEntities as getTickets } from 'app/entities/ticket/ticket.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IMessage } from 'app/shared/model/message.model';
import { getEntity, updateEntity, createEntity, reset } from './message.reducer';

export const MessageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const tickets = useAppSelector(state => state.ticket.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const messageEntity = useAppSelector(state => state.message.entity);
  const loading = useAppSelector(state => state.message.loading);
  const updating = useAppSelector(state => state.message.updating);
  const updateSuccess = useAppSelector(state => state.message.updateSuccess);

  const handleClose = () => {
    navigate('/message' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTickets({}));
    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.sentAt = convertDateTimeToServer(values.sentAt);

    const entity = {
      ...messageEntity,
      ...values,
      ticket: tickets.find(it => it.id.toString() === values.ticket.toString()),
      sentBy: users.find(it => it.id.toString() === values.sentBy.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          sentAt: displayDefaultDateTime(),
        }
      : {
          ...messageEntity,
          sentAt: convertDateTimeFromServer(messageEntity.sentAt),
          ticket: messageEntity?.ticket?.id,
          sentBy: messageEntity?.sentBy?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ticketsManagementApp.message.home.createOrEditLabel" data-cy="MessageCreateUpdateHeading">
            <Translate contentKey="ticketsManagementApp.message.home.createOrEditLabel">Create or edit a Message</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="message-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('ticketsManagementApp.message.content')}
                id="message-content"
                name="content"
                data-cy="content"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketsManagementApp.message.sentAt')}
                id="message-sentAt"
                name="sentAt"
                data-cy="sentAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="message-ticket"
                name="ticket"
                data-cy="ticket"
                label={translate('ticketsManagementApp.message.ticket')}
                type="select"
              >
                <option value="" key="0" />
                {tickets
                  ? tickets.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="message-sentBy"
                name="sentBy"
                data-cy="sentBy"
                label={translate('ticketsManagementApp.message.sentBy')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/message" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MessageUpdate;
