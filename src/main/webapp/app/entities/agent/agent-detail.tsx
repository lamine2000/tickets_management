import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './agent.reducer';

export const AgentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const agentEntity = useAppSelector(state => state.agent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="agentDetailsHeading">
          <Translate contentKey="ticketsManagementApp.agent.detail.title">Agent</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{agentEntity.id}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="ticketsManagementApp.agent.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{agentEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="ticketsManagementApp.agent.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{agentEntity.lastName}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="ticketsManagementApp.agent.email">Email</Translate>
            </span>
          </dt>
          <dd>{agentEntity.email}</dd>
          <dt>
            <Translate contentKey="ticketsManagementApp.agent.user">User</Translate>
          </dt>
          <dd>{agentEntity.user ? agentEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/agent" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/agent/${agentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AgentDetail;
