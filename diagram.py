from diagrams import Diagram, Cluster, Edge
from diagrams.onprem.queue import Rabbitmq
from diagrams.onprem.container import Docker
from diagrams.onprem.network import Nginx
from diagrams.k8s.compute import Pod

with Diagram("Run Architecture", show=False, graph_attr={"bgcolor": "transparent"}):
    ingress = Nginx("Ingress", fontcolor="#38BDF8")
    frontend = Docker("Frontend", fontcolor="#38BDF8")
    api = Docker("API Websocket", fontcolor="#38BDF8")
    queue = Rabbitmq("Job Queue", fontcolor="#38BDF8")

    ingress >> frontend
    ingress >> api

    api >> queue    

    with Cluster("Workers"):
        workers = [
            Pod("Worker"),
            Pod("Worker"),
            Pod("Worker")
        ]        
                
        queue >> workers
        
    workers[0] >> Edge(color="#38BDF8") << Docker("Job", fontcolor="#38BDF8")
    workers[1] >> Edge(color="#38BDF8") << Docker("Job", fontcolor="#38BDF8")
    workers[2] >> Edge(color="#38BDF8") << Docker("Job", fontcolor="#38BDF8")