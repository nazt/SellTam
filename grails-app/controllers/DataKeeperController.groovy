class DataKeeperController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
        [dataKeeperInstanceList: DataKeeper.list(params), dataKeeperInstanceTotal: DataKeeper.count()]
    }

    def create = {
        def dataKeeperInstance = new DataKeeper()
        dataKeeperInstance.properties = params
        return [dataKeeperInstance: dataKeeperInstance]
    }

    def save = {
        def dataKeeperInstance = new DataKeeper(params)
        if (dataKeeperInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'dataKeeper.label', default: 'DataKeeper'), dataKeeperInstance.id])}"
            redirect(action: "show", id: dataKeeperInstance.id)
        }
        else {
            render(view: "create", model: [dataKeeperInstance: dataKeeperInstance])
        }
    }

	def natsave = {
			def dataKeeperInstance = new DataKeeper(name:params['name'],surname:params['surname'])
			 dataKeeperInstance.save()
			 render 'save completed!'
	}
    def show = {
        def dataKeeperInstance = DataKeeper.get(params.id)
        if (!dataKeeperInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dataKeeper.label', default: 'DataKeeper'), params.id])}"
            redirect(action: "list")
        }
        else {
            [dataKeeperInstance: dataKeeperInstance]
        }
    }

    def edit = {
        def dataKeeperInstance = DataKeeper.get(params.id)
        if (!dataKeeperInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dataKeeper.label', default: 'DataKeeper'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [dataKeeperInstance: dataKeeperInstance]
        }
    }

    def update = {
        def dataKeeperInstance = DataKeeper.get(params.id)
        if (dataKeeperInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (dataKeeperInstance.version > version) {
                    
                    dataKeeperInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'dataKeeper.label', default: 'DataKeeper')] as Object[], "Another user has updated this DataKeeper while you were editing")
                    render(view: "edit", model: [dataKeeperInstance: dataKeeperInstance])
                    return
                }
            }
            dataKeeperInstance.properties = params
            if (!dataKeeperInstance.hasErrors() && dataKeeperInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'dataKeeper.label', default: 'DataKeeper'), dataKeeperInstance.id])}"
                redirect(action: "show", id: dataKeeperInstance.id)
            }
            else {
                render(view: "edit", model: [dataKeeperInstance: dataKeeperInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dataKeeper.label', default: 'DataKeeper'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def dataKeeperInstance = DataKeeper.get(params.id)
        if (dataKeeperInstance) {
            try {
                dataKeeperInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'dataKeeper.label', default: 'DataKeeper'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'dataKeeper.label', default: 'DataKeeper'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dataKeeper.label', default: 'DataKeeper'), params.id])}"
            redirect(action: "list")
        }
    }
}
