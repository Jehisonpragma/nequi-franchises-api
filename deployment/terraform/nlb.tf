terraform {

}

/* resource "aws_lb" "nlb" {
  name               = "ecs-nlb"
  internal           = false
  load_balancer_type = "network"
  subnets            = [aws_subnet.private_1.id, aws_subnet.private_2.id] # replace with your subnets
}

resource "aws_lb_target_group" "ecs_tg" {
  name        = "ecs-tg"
  port        = 8081
  protocol    = "TCP"
  vpc_id      = aws_vpc.main.id # replace with your VPC
  target_type = "ip" 

  health_check {
    protocol = "TCP"
  }
}

resource "aws_lb_listener" "ecs_listener" {
  load_balancer_arn = aws_lb.nlb.arn
  port              = 8081
  protocol          = "TCP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.ecs_tg.arn
  }
} */
