terraform {

}

# ECR 
resource "aws_ecr_repository" "ecr-api-franchises-nequi" {
  name                 = "api-franchises-nequi"
  image_tag_mutability = "MUTABLE"   # or "IMMUTABLE" to prevent overwriting tags

  image_scanning_configuration {
    scan_on_push = true
  }

  tags = {
    Environment = "dev"
    Project     = "api-franchises-nequi"
  }
}