class TasksController < ApplicationController
  before_action :check_token
  before_action :set_task, only: %i[ show update destroy complete uncomplete]

  def index
    @tasks = Task.all

    render json: @tasks
  end

  def show
    render json: @task
  end

  def create
    @task = Task.new(task_params)
    @task.username = @current_username

    if @task.save
      render json: @task, status: :created, location: @task
    else
      render json: @task.errors, status: :unprocessable_entity
    end
  end

  def update
    if @task.update(task_params)
      render json: @task
    else
      render json: @task.errors, status: :unprocessable_entity
    end
  end

  def destroy
    @task.destroy!
  end

  def complete
    @task.complete = true

    if @task.save
      render json: @task
    else
      render json: @task.errors, status: :unprocessable_entity
    end
  end

  def uncomplete
    @task.complete = false

    if @task.save
      render json: @task
    else
      render json: @task.errors, status: :unprocessable_entity
    end
  end

  private
    def check_token
      token = request.headers['Authorization']
      begin
        decoded = JWT.decode(token, Rails.application.credentials.secret_key_jwt)
        @current_username = decoded[0]['username']
      rescue JWT::ExpiredSignature => e
        render json: { error: 'Expired token'}, status: 401
      rescue JWT::DecodeError => e
        render json: { error: 'Invalid token'}, status: 401
      end
    end

    def set_task
      @task = Task.find(params.expect(:id))
    end

    def task_params
      params.expect(task: [ :title, :description, :complete ])
    end
end
