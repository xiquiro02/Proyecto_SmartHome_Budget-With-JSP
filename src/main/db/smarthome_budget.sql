create database SmartHome_BudgetBD;
use SmartHome_BudgetBD;
show databases;

create table Hogar(
IDHogar int auto_increment not null, 
NombreHogar varchar(100) not null, 
primary key (IDHogar));

create table Roles (
IDRol int auto_increment not null,
NombreRol varchar(100) not null,
primary key (IDRol));

create table PermisosRoles (
IDPermiso int auto_increment not null,
NombrePermiso varchar(100) not null,
Modulo varchar(100) not null,
primary key (IDPermiso));

create table DetallesPermisos (
IDDetallesPermiso int auto_increment not null,
IDRol int not null,
IDPermiso int not null,
primary key (IDDetallesPermiso),
foreign key (IDRol) references Roles(IDRol),
foreign key (IDPermiso) references PermisosRoles(IDPermiso));

create table Usuario (
IDUsuario int auto_increment not null,
NombreUsuario varchar(100) not null,
PrimerApellido varchar(100) not null,
SegundoApellido varchar(100),
correo varchar(100) not null unique,
telefono varchar(20) not null,
ContrasenaUsuario varchar(100) not null,
FechaRegistro datetime,
primary key (IDUsuario));

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

create table Categorias_Egresos (
IDCategoriaEgreso int auto_increment not null,
NombreCategoriaEgreso varchar(100) not null,
primary key (IDCategoriaEgreso));

create table Metodo_Pago (
IDMetodoPago int auto_increment not null,
NombreMetodoPago varchar(100) not null,
primary key (IDMetodoPago));

create table Registro_Egresos (
IDEgresos int auto_increment not null,
IDHogar int not null,
IDUsuario int not null,
Monto decimal(12,2) not null,
IDCategoriaEgreso int not null, 
IDMetodoPago int not null,
FechaVencimiento datetime not null,
Descripcion text, 
EstadoPago enum("Pendiente", "Pagada", "Vencida") default "Pendiente",
primary key (IDEgresos),
foreign key (IDHogar) references Hogar(IDHogar), 
foreign key (IDUsuario) references Usuario(IDUsuario),
foreign key (IDCategoriaEgreso) references Categorias_Egresos(IDCategoriaEgreso),
foreign key (IDMetodoPago) references Metodo_Pago(IDMetodoPago));

create table Recordatorios_Egresos (
IDRecordatorioEgresos int auto_increment not null, 
IDEgresos int not null,
IDUsuario int not null,
DescripcionAlerta text, 
FechaRecordatorio datetime not null,
primary key (IDRecordatorioEgresos),
foreign key (IDUsuario) references Usuario(IDUsuario),
foreign key (IDEgresos) references Registro_Egresos(IDEgresos));

create table Categorias_Ingresos (
IDCategoriaIngreso int auto_increment not null, 
NombreCategoriaIngreso varchar(100) not null,
primary key (IDCategoriaIngreso));

create table Registro_Ingresos (
IDIngresos int auto_increment not null,
IDHogar int not null,
Monto decimal(12,2) not null,
FechaIngreso datetime not null,
Descripcion text,
IDCategoriaIngreso int not null, 
primary key (IDIngresos),
foreign key (IDHogar) references Hogar(IDHogar),
foreign key (IDCategoriaIngreso) references Categorias_Ingresos(IDCategoriaIngreso));

create table Recordatorios_Ingresos (
IDRecordatorioIngresos int auto_increment not null, 
IDIngresos int not null,
IDUsuario int not null,
DescripcionAlerta text, 
FechaRecordatorio datetime not null,
primary key (IDRecordatorioIngresos),
foreign key (IDUsuario) references Usuario(IDUsuario),
foreign key (IDIngresos) references Registro_Ingresos(IDIngresos));

create table Coordenadas (
IDCoordenada int auto_increment not null,
Latitud decimal(12,8),
Logitud decimal(12,8),
primary key (IDCoordenada));

create table Ubicacion_Usuario (
IDUbicacion int auto_increment not null,
Pais varchar(100) not null,
Ciudad varchar(100) not null,
Direccion varchar(100) not null,
FechaRegistro datetime not null,
IDUsuario int not null, 
IDCoordenada int,
primary key (IDUbicacion),
foreign key (IDCoordenada) references Coordenadas(IDCoordenada));

create table Tienda (
IDTienda int auto_increment not null,
NombreTienda varchar(100) not null,
Pais varchar(100) not null,
Ciudad varchar(100) not null,
Direccion varchar(100) not null,
IDCoordenada int,
FechaRegistro datetime, 
primary key(IDTienda),
foreign key (IDCoordenada) references Coordenadas(IDCoordenada));

create table Tipo_Producto (
IDTipoProducto int auto_increment not null,
NombreTipoProducto enum("Otros", "Aseo", "Alimentos", "Personal", "Ropa y calzado") default "Otros",
primary key (IDTipoProducto));

create table Producto (
IDProducto int auto_increment not null,
NombreProducto varchar(100) not null,
Descripcion text,
IDTipoProducto int not null,
StockInicial int not null,
StockMinimo int not null,  
primary key(IDProducto),
foreign key (IDTipoProducto) references Tipo_Producto(IDTipoProducto));

create table Inventario_Casa (
IDInventario int auto_increment not null,
IDProducto int not null,
IDHogar int not null,
StockActual int not null, 
FechaActualizacion datetime not null,
primary key(IDInventario),
foreign key (IDHogar) references Hogar(IDHogar),
foreign key (IDProducto) references Producto(IDProducto));

create table Precio_Producto (
IDPrecio int auto_increment not null,
IDTienda int not null,
IDInventario int not null,
IDHogar int not null,
precio decimal(12,2) not null,
FechaActualizacion datetime not null,
primary key (IDPrecio),
foreign key (IDTienda) references Tienda(IDTienda),
foreign key (IDInventario) references Inventario_Casa(IDInventario),
foreign key (IDHogar) references Hogar(IDHogar));

create table Lista_Compras (
IDListaCompras int auto_increment not null,
NombreLista varchar(100) not null,
IDProducto int not null,
IDHogar int not null,
FechaCreacion datetime,
primary key(IDListaCompras),
foreign key (IDProducto) references Producto(IDProducto),
foreign key (IDHogar) references Hogar(IDHogar));

create table Recordatorios (
IDRecordatorio int auto_increment not null, 
IDListaCompras int not null,
IDUsuario int not null,
IDHogar int not null, 
DescripcionAlerta text, 
FechaRecordatorio datetime not null,
primary key (IDRecordatorio),
foreign key (IDUsuario) references Usuario(IDUsuario),
foreign key (IDHogar) references Hogar(IDHogar),
foreign key (IDListaCompras) references Lista_Compras(IDListaCompras));
