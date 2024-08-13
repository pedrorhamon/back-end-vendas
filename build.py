import os
import threading

threads = []


def build_application(app):
    with lock:
        threads.append(app)
    print(f"Building application {app}")
    subprocess.run(f"cd {app} && mvn clean package -DskipTests", shell=True, check=True)
    print(f"Application {app} finished building!")
    with lock:
        threads.remove(app)


def docker_compose_up():
    print("Running containers!")
    os.popen("docker-compose up --build -d").read()
    print("Pipeline finished!")


def build_all_applications():
    print("Starting to build applications!")
    threading.Thread(target=build_application,
                     args={"back-end-vendas"}).start()


def remove_remaining_containers():
    print("Removing all containers.")
    os.system("docker-compose down")
    containers = os.popen('docker ps -aq').read().split('\n')
    containers.remove('')
    if len(containers) > 0:
        print("There are still {} containers created".format(containers))
        for container in containers:
            print("Stopping container {}".format(container))
            os.system("docker container stop {}".format(container))
        os.system("docker container prune -f")


if __name__ == "__main__":
    print("Pipeline started!")
    build_all_applications()
    while len(threads) > 0:
        pass
    remove_remaining_containers()
    threading.Thread(target=docker_compose_up).start()
