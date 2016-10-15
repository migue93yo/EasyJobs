package com.miguel.easyjobs.model;

public class TablesDataSource {

    //Script de Creación de la tab las
    protected static final String CREATE_EMPLOYEE_TABLE = "CREATE TABLE Trabajador (" +
            "  email       char(100)," +
            "  contrasenia char(12) NOT NULL," +
            "  nombre      char(50) NOT NULL," +
            "  provincia   char(20) NOT NULL," +
            "  f_nac       date NOT NULL," +
            "  telefono    char(9) NOT NULL," +
            "  CONSTRAINT TRABAJADOR PRIMARY KEY (email)" +
            ")";

    protected static final String CREATE_COMPANY_TABLE = "CREATE TABLE Empresa (" +
            "  email       char(100)," +
            "  contrasenia integer(12) NOT NULL," +
            "  nombre      char(50) NOT NULL," +
            "  descripcion text," +
            "  provincia   char(20) NOT NULL," +
            "  CONSTRAINT PK_EMPRESA PRIMARY KEY (email)" +
            ")";

    protected static final String CREATE_OFFER_TABLE = "CREATE TABLE Oferta (" +
            "  id            integer," +
            "  empresa_email char(100) NOT NULL," +
            "  titulo        char(150) NOT NULL," +
            "  provincia     char(20) NOT NULL," +
            "  puesto        char(15) NOT NULL," +
            "  duracion      date NOT NULL," +
            "  tipo_contrato char(26) NOT NULL," +
            "  jornada       char(22) NOT NULL," +
            "  descripcion   text NOT NULL," +
            "  estado        tinyint(1) DEFAULT 1 NOT NULL," +
            "  CONSTRAINT PK_OFERTA PRIMARY KEY (id)," +
            "  CONSTRAINT FK_OFERTA_EMPRESA FOREIGN KEY (empresa_email) REFERENCES Empresa (email)," +
            "  CONSTRAINT CK_OFERTA_TIPOCONTRATO CHECK(tipo_contrato IN (\"Indefinido\", \"Temporal\", \"En prácticas\"))," +
            "  CONSTRAINT CK_OFERTA_JORNADAS CHECK(jornada IN (\"Jornada completa\", \"Media jornada\", \"Jornada parcial\"))" +
            ")";

    protected static final String CREATE_INSCRIPTION_TABLE = "CREATE TABLE Inscripcion (" +
            "  oferta_id          integer," +
            "  trabajador_email   char(100) NOT NULL," +
            "  estado             char(12) DEFAULT 'Inscrito' NOT NULL," +
            "  CONSTRAINT PK_INSCRIPCION PRIMARY KEY (oferta_id, trabajador_email)," +
            "  CONSTRAINT FK_INSCRIPCION_TRABAJADOR FOREIGN KEY (trabajador_email) REFERENCES Trabajador (email)," +
            "  CONSTRAINT FK_INSCRIPCION_OFERTA FOREIGN KEY (oferta_id) REFERENCES Oferta (id)," +
            "  CONSTRAINT CK_INSCRIPCION_ESTADO CHECK(estado IN (\"Inscrito\", \"Seleccionado\", \"Finalizado\"))" +
            ")";

    protected static final String CREATE_EXPERIENCE_TABLE = "CREATE TABLE Experiencia (" +
            "  id                integer," +
            "  trabajador_email  char(100) NOT NULL," +
            "  puesto            char(50) NOT NULL," +
            "  empresa           char(50) NOT NULL," +
            "  provincia         char(20) NOT NULL," +
            "  f_ini             date NOT NULL," +
            "  f_fin             date," +
            "  CONSTRAINT PK_EXPERIENCIA PRIMARY KEY (id)," +
            "  CONSTRAINT FK_EXPERIENCIA_TRABAJADOR FOREIGN KEY (trabajador_email) REFERENCES Trabajador (email)" +
            ")";

    protected static final String CREATE_TITLE_TABLE = "CREATE TABLE Titulo (" +
            "  id                integer," +
            "  trabajador_email  char(100) NOT NULL," +
            "  titulo            char(150) NOT NULL," +
            "  centro            char(50) NOT NULL," +
            "  provincia         char(20) NOT NULL," +
            "  f_ini             date NOT NULL," +
            "  f_fin             date," +
            "  CONSTRAINT PK_TITULO PRIMARY KEY (id)," +
            "  CONSTRAINT FK_TITULO_TRABAJADOR FOREIGN KEY (trabajador_email) REFERENCES Trabajador (email)" +
            ")";

    protected static final String CREATE_MESSAGE_TABLE = "CREATE TABLE Mensaje (" +
            "  empresa_email     char(100)," +
            "  trabajadores_email char(100)," +
            "  mensaje           text NOT NULL," +
            "  fecha             date NOT NULL," +
            "  hora              time NOT NULL," +
            "  leido             tinyint(1) DEFAULT 0 NOT NULL," +
            "  CONSTRAINT FK_MENSAJES_EMPRESAS FOREIGN KEY (empresa_email) REFERENCES Empresas (email)," +
            "  CONSTRAINT FK_MENSAJES_TRABAJADORES FOREIGN KEY (trabajadores_email) REFERENCES Trabajadores (email)" +
            ")";

    //Para pruebas
    protected static final String ADMIN_EMPLOYEE = "INSERT INTO Trabajador " +
            "VALUES(\"admin\", \"admin\",\"Miguel Ángel Conde Portillo\", \"Sevilla\", '18-06-1993', 616851325)";

    protected static final String ADMIN_COMPANY = "INSERT INTO Empresa " +
            "VALUES(\"admin2\", \"admin2\", \"Miguel Ángel Conde Portillo\", \"DESC\", \"Sevilla\")";

    protected static final String ADMIN_EXPERIENCES_TODAY = "INSERT INTO Experiencia(trabajador_email, puesto, empresa, provincia, f_ini, f_fin) " +
            "VALUES(\"admin\", \"Desarrollador de JSF\", \"Guadaltel S.A.\", \"Sevilla\",'18-06-1993','11-11-1111')," +
            "(\"admin\", \"Analista\", \"Nuevas Profesiones\", \"Sevilla\", '18-06-1993', '15-02-2015')";

    protected static final String ADMIN_EXPERIENCES = "INSERT INTO Experiencia(trabajador_email, puesto, empresa, provincia, f_ini, f_fin) " +
            "VALUES(\"admin\", \"Analista\", \"Nuevas Profesiones\", \"Sevilla\", '18-06-1993', '18-06-1993')";

    protected static final String ADMIN_TITLES = "INSERT INTO Titulo(trabajador_email, titulo, centro, provincia, f_ini, f_fin) " +
            "VALUES(\"admin\", \"Desarrollador de aplicaciones multiplataforma\", \"Nuevas Profesiones\", \"Sevilla\", '18-06-1993', '11-11-1111')," +
            "(\"admin\", \"Artes escénicas\", \"Ramón Carande\", \"Sevilla\", \"18-06-1993\", \"11-11-1111\")";

    protected static final String ADMIN_NEW_OFFERS = "INSERT INTO oferta" +
            " VALUES(0, \"admin2\", \"Desarrollador java\", \"Sevilla\", \"Desarrollador\", \"18-06-2018\", \"Temporal\",  \"Media jornada\"" +
            ", \"Buscamos un trabajador capacidad de aprendizaje y trabajo en equipo para participar en nuestros proyectos, " +
            "se requieren conocimientos de java y JSF\", \"1\")";

}