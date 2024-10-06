variable "aws_region" {
  description = "Región de AWS donde se desplegarán los recursos"
  default     = "us-east-1"
}

variable "db_name" {
  description = "Nombre de la base de datos"
  default     = "franquiciasdb"
}

variable "db_username" {
  description = "Usuario de la base de datos"
  default     = "demo"
}

variable "db_password" {
  description = "Contraseña para el usuario de la base de datos"
  default     = "fraquiciasdemo"
  sensitive   = true
}
