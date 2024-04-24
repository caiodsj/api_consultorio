create table pacientes(

    id bigserial not null ,
    nome varchar(100) not null,
    email varchar(100) not null unique,
    cpf varchar(14) not null unique,
    logradouro varchar(100) not null,
    bairro varchar(100) not null,
    cep varchar(9) not null,
    complemento varchar(100),
    numero varchar(20),
    uf char(2) not null,
    cidade varchar(100) not null,
    telefone varchar(20) not null,
    ativo boolean not null,

    primary key(id)

);


create table consultas(

    id bigserial not null primary key,
    medico_id bigserial not null,
    paciente_id bigserial not null,
    data timestamp not null,

    constraint fk_consultas_medico_id foreign key(medico_id) references medicos(id),
    constraint fk_consultas_paciente_id foreign key(paciente_id) references pacientes(id)

);