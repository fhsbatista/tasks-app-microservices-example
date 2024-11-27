class Task
  include Mongoid::Document
  include Mongoid::Timestamps
  
  field :title, type: String
  field :description, type: String
  field :complete, type: Mongoid::Boolean
end
