CREATE TABLE model (
    created DATETIME,
    deploymentKey VARCHAR(255),
    fileName VARCHAR(255)
);

CREATE TABLE activity (
    created DATETIME,
    processInstanceId VARCHAR(255),
    activityId VARCHAR(255),
    activityInstanceId VARCHAR(255),
    lifecycleType VARCHAR(255),
    endDate DATETIME
);

CREATE TABLE deployment(
    created DATETIME,
    deploymentId VARCHAR(255),
    xml CLOB,
    fileName VARCHAR(255),
    deploymentKey VARCHAR(255)
);

CREATE TABLE process(
    created DATETIME,
    processDefinitionKey VARCHAR(255),
    processDefinitionId VARCHAR(255),
    businessKey VARCHAR(255),
    processInstanceId VARCHAR(255),
    superProcessInstanceId VARCHAR(255),
    lifecycleType VARCHAR(255),
    endDate DATETIME
);

CREATE TABLE variable(
    created DATETIME,
    processInstanceId VARCHAR(255),
    variableName VARCHAR(255),
    textValue VARCHAR(1024),
    serializerName VARCHAR(255)
);