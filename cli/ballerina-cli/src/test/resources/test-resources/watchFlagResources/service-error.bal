public function main() {}

public class Listener {
    int port;
    public function 'start() returns error? {}

    public function gracefulStop() returns error? {}

    public function immediateStop() returns error? {}

    public function detach(service object {} s) returns error? {}

    public function attach(service object {} s, string[]? name = ()) returns error? {}

    public function init(int port) {
        self.port = port // Missing colon here
    }
}

listener Listener 'listener = new Listener(9000);

service / on 'listener {
    function init() {}
}
