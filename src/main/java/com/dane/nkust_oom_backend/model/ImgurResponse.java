package com.dane.nkust_oom_backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImgurResponse {

    private boolean success;
    private int status;
    private Data data;

    public static class Data {
        private String id;
        private String title;
        private String description;
        private String link;

        @JsonProperty("deletehash")
        private String deleteHash;

        // Getters and Setters
        public String getId() { 
            return id; 
        }

        public void setId(String id) { 
            this.id = id; 
        }
        
        public String getTitle() { 
            return title; 
        }

        public void setTitle(String title) { 
            this.title = title; 
        }
        
        public String getDescription() { 
            return description; 
        }
        
        public void setDescription(String description) { 
            this.description = description; 
        }
        
        public String getLink() { 
            return link; 
        }
        
        public void setLink(String link) { 
            this.link = link; 
        }
        
        public String getDeleteHash() { 
            return deleteHash; 
        }
        
        public void setDeleteHash(String deleteHash) { 
            this.deleteHash = deleteHash; 
        }
    }

    // Getters and Setters
    public boolean isSuccess() { 
        return success; 
    }
    
    public void setSuccess(boolean success) { 
        this.success = success; 
    }
    
    public int getStatus() { 
        return status; 
    }
    
    public void setStatus(int status) { 
        this.status = status; 
    }
    
    public Data getData() { 
        return data; 
    }
    
    public void setData(Data data) { 
        this.data = data; 
    }
} 