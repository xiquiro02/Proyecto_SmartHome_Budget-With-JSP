create database SmartHome_BudgetBD;
use SmartHome_BudgetBD;
show databases;

-- Gestión de usuarios (AUTENTICACIÓN)

create table Hogar(
IDHogar int auto_increment not null, 
NombreHogar varchar(100) not null,
primary key (IDHogar));

create table Roles (
IDRol int auto_increment not null,
NombreRol varchar(100) not null,
Descripcion text not null,
primary key (IDRol));

create table Permisos(
IDPermiso int auto_increment not null,
NombrePermiso varchar(100) not null,
Modulo varchar(100) not null,
primary key (IDPermiso));

create table DetallesPermisos (
IDDetallesPermiso int auto_increment not null,
IDRol int not null,
IDPermiso int not null,
unique (IDRol, IDPermiso),
primary key (IDDetallesPermiso),
foreign key (IDRol) references Roles(IDRol),
foreign key (IDPermiso) references Permisos(IDPermiso));

create table Usuario (
IDUsuario int auto_increment not null,
Documento varchar(20) unique not null,
NombreUsuario varchar(100) not null,
Apellido varchar(100) not null,
correo varchar(100) unique not null,
telefono varchar(20) not null,
fotoPerfil varchar(255) null,
ContrasenaUsuario varchar(255) not null,
FechaRegistro datetime default current_timestamp,
primary key (IDUsuario));

create table Tokens(
IDtoken int auto_increment not null,
tokenRecuperacion varchar(255) not null unique,
FechaExpiracion datetime not null,
Usado boolean default false,
IDUsuario int not null,
primary key (IDtoken),
foreign key (IDUsuario) references Usuario(IDUsuario));

create table CodigosInvitacion (
IDCodigo int auto_increment not null,
Codigo varchar(50) not null unique,
FechaCreacion datetime not null,
FechaExpiracion datetime not null,
Estado enum('Activo','Usado','Expirado') default 'Activo',
IDHogar int not null,
IDRol int not null,
primary key (IDCodigo),
foreign key (IDHogar) references Hogar(IDHogar),
foreign key (IDRol) references Roles(IDRol));

create table DetallesHogares(
IDDetallesHogar int auto_increment not null,
IDUsuario int not null,
IDHogar int not null,
IDRol int not null,
primary key (IDDetallesHogar),
unique (IDUsuario, IDHogar),
foreign key (IDUsuario) references Usuario(IDUsuario),
foreign key (IDHogar) references Hogar(IDHogar), 
foreign key (IDRol) references Roles(IDRol));

-- Gestión Facturas y Finanzas 	

create table Categorias_Egresos (
IDCategoriaEgreso int auto_increment not null,
NombreCategoriaEgreso varchar(100) unique not null,
primary key (IDCategoriaEgreso));

create table Metodo_Pago (
IDMetodoPago int auto_increment not null,
NombreMetodoPago varchar(100) unique not null,
primary key (IDMetodoPago));

create table Registro_Egresos (
IDEgresos int auto_increment not null,
DescripcionPago text not null,
Monto decimal(12,2) not null,
FechaPago datetime null,
FechaVencimiento datetime not null,
EstadoPago enum("Pendiente", "Pagada", "Vencida") default "Pendiente",
EstadoEgresos enum("Activo", "Anulado") default "Activo",
IDHogar int not null,
IDCategoriaEgreso int not null, 
IDMetodoPago int not null,
primary key (IDEgresos),
foreign key (IDHogar) references Hogar(IDHogar), 
foreign key (IDCategoriaEgreso) references Categorias_Egresos(IDCategoriaEgreso),
foreign key (IDMetodoPago) references Metodo_Pago(IDMetodoPago));

create table Categorias_Ingresos (
IDCategoriaIngreso int auto_increment not null, 
NombreCategoriaIngreso varchar(100) unique not null,
primary key (IDCategoriaIngreso));

create table Registro_Ingresos (
IDIngresos int auto_increment not null,
Monto decimal(12,2) not null,
FechaIngreso datetime not null,
Descripcion text,
EstadoIngreso enum("Activo", "Anulado") default "Activo",
IDHogar int not null,
IDCategoriaIngreso int not null, 
primary key (IDIngresos),
foreign key (IDHogar) references Hogar(IDHogar),
foreign key (IDCategoriaIngreso) references Categorias_Ingresos(IDCategoriaIngreso));

create table Presupuesto_Mensual (
IDPresupuesto int auto_increment not null,
MontoMax decimal(12,2) not null,
Mes int not null,
Anio int not null,
FechaCreacion datetime null default current_timestamp,
IDHogar int not null,
primary key (IDPresupuesto),
unique (IDHogar, Mes, Anio), -- Evita que por error alguien inserte dos presupuestos para el mismo mes en la misma casa.
foreign key (IDHogar) references Hogar(IDHogar));

-- Inventario

create table Tipo_Producto (
IDTipoProducto int auto_increment not null,
NombreTipoProducto varchar(100) unique not null,
primary key (IDTipoProducto));

create table Producto (
IDProducto int auto_increment not null,
NombreProducto varchar(100) not null,
Descripcion text,
IDTipoProducto int not null,
primary key(IDProducto),
unique (NombreProducto, IDTipoProducto),
foreign key (IDTipoProducto) references Tipo_Producto(IDTipoProducto));

create table Inventario_Casa (
IDInventario int auto_increment not null,
StockActual decimal(10,2) not null, 
FechaActualizacion datetime not null default current_timestamp on update current_timestamp,
IDProducto int not null,
IDHogar int not null,
primary key(IDInventario),
unique (IDHogar, IDProducto),
foreign key (IDHogar) references Hogar(IDHogar),
foreign key (IDProducto) references Producto(IDProducto));

-- Listas de Compras

create table Lista_Compras (
IDListaCompras int auto_increment not null,
NombreLista varchar(100) not null,
FechaCreacion datetime default current_timestamp,
EstadoLista enum('Pendiente','En progreso','Completa') default 'Pendiente',
IDHogar int not null,
primary key(IDListaCompras),
foreign key (IDHogar) references Hogar(IDHogar));

create table Detalle_ListaCompras (
IDDetalleLista int auto_increment not null,
Cantidad decimal(10,2) not null default 1.00,
Comprado boolean default false, 
IDListaCompras int not null,
IDProducto int not null,
primary key (IDDetalleLista),
unique (IDListaCompras, IDProducto),
foreign key (IDListaCompras) references Lista_Compras(IDListaCompras),
foreign key (IDProducto) references Producto(IDProducto));
