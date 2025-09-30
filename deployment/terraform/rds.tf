terraform {

}

//RDS

//Subnets
# Create subnets in the VPC
/* resource "aws_subnet" "subnet_db_1" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.5.0/24"
  availability_zone = "us-east-1a"
}

resource "aws_subnet" "subnet_db_2" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.6.0/24"
  availability_zone = "us-east-1b"
}
 */
# Create a DB Subnet Group for RDS, linked to the VPC via its subnets
resource "aws_db_subnet_group" "rds_subnet_group" {
  name       = "db-subnet-group"
  subnet_ids = [aws_subnet.public_1.id, aws_subnet.public_2.id]

  tags = {
    Name = "db-subnet-group"
  }
}

# Security Group to allow inbound Postgres traffic (optional for testing)
resource "aws_security_group" "rds_sg" {
  name        = "rds-postgres-sg"
  description = "Allow Postgres traffic"
  vpc_id = aws_vpc.main.id

  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # ⚠️ Not secure! Restrict to your IP for production
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# RDS PostgreSQL Instance (free tier eligible)
resource "aws_db_instance" "postgres" {
  identifier             = "my-postgres-db"
  allocated_storage      = 20               # Free tier max
  max_allocated_storage  = 20
  engine                 = "postgres"
  engine_version         = "17.4"           # adjust to supported version
  instance_class         = "db.t4g.micro"    # free tier
  username               = aws_ssm_parameter.db_username.value
  password               = aws_ssm_parameter.db_password.value # store in SSM Parameter Store ideally
  parameter_group_name   = "default.postgres17"
  skip_final_snapshot    = true             # avoid snapshot costs
  publicly_accessible    = true             # for quick test, not secure
  db_subnet_group_name = aws_db_subnet_group.rds_subnet_group.name
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
}

resource "aws_ssm_parameter" "db_host" {
  name        = "/config/common/adapters/r2dbc/host"
  description = "Database Postgres Host"
  type        = "String"
  value       = aws_db_instance.postgres.address
}

resource "aws_ssm_parameter" "db_port" {
  name        = "/config/common/adapters/r2dbc/port"
  description = "Database Postgres Port"
  type        = "String"
  value       = "5432"
}

resource "aws_ssm_parameter" "db_database" {
  name        = "/config/common/adapters/r2dbc/database"
  description = "Database Postgres Database name"
  type        = "String"
  value       = "postgres"
}

resource "aws_ssm_parameter" "db_schema" {
  name        = "/config/common/adapters/r2dbc/schema"
  description = "Database Postgres Schema"
  type        = "String"
  value       = "public"
}

resource "aws_ssm_parameter" "db_username" {
  name        = "/config/common/adapters/r2dbc/username"
  description = "Database Postgres username for MyApp"
  type        = "String"
  value       = "postgres"
}

resource "aws_ssm_parameter" "db_password" {
  name        = "/config/common/adapters/r2dbc/password"
  description = "Database Postgres password for MyApp"
  type        = "String"
  value       = "somePassword"
}