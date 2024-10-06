output "rds_endpoint" {
  description = "El endpoint de la instancia de RDS"
  value       = aws_db_instance.franquicias_rds_instance.endpoint
}

output "rds_username" {
  description = "El nombre de usuario de la base de datos RDS"
  value       = var.db_username
}

output "rds_db_name" {
  description = "El nombre de la base de datos"
  value       = var.db_name
}
