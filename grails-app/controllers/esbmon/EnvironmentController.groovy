package esbmon

class EnvironmentController {

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index = {
    redirect(action: "list", params: params)
  }

  def list = {
    params.max = Math.min(params.max ? params.int('max') : 10, 100)
    [environmentInstanceList: Environment.list(params), environmentInstanceTotal: Environment.count()]
  }

  def create = {
    def environmentInstance = new Environment()
    environmentInstance.properties = params
    return [environmentInstance: environmentInstance]
  }

  def save = {
    def environmentInstance = new Environment(params)
    if (environmentInstance.save(flush: true)) {
      flash.message = "${message(code: 'default.created.message', args: [message(code: 'environment.label', default: 'Environment'), environmentInstance.id])}"
      redirect(action: "show", id: environmentInstance.id)
    }
    else {
      render(view: "create", model: [environmentInstance: environmentInstance])
    }
  }

  def show = {
    def environmentInstance = Environment.get(params.id)
    if (!environmentInstance) {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'environment.label', default: 'Environment'), params.id])}"
      redirect(action: "list")
    }
    else {
      [environmentInstance: environmentInstance]
    }
  }

  def edit = {
    def environmentInstance = Environment.get(params.id)
    if (!environmentInstance) {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'environment.label', default: 'Environment'), params.id])}"
      redirect(action: "list")
    }
    else {
      return [environmentInstance: environmentInstance]
    }
  }

  def update = {
    def environmentInstance = Environment.get(params.id)
    if (environmentInstance) {
      if (params.version) {
        def version = params.version.toLong()
        if (environmentInstance.version > version) {

          environmentInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'environment.label', default: 'Environment')] as Object[], "Another user has updated this Environment while you were editing")
          render(view: "edit", model: [environmentInstance: environmentInstance])
          return
        }
      }
      environmentInstance.properties = params
      if (!environmentInstance.hasErrors() && environmentInstance.save(flush: true)) {
        flash.message = "${message(code: 'default.updated.message', args: [message(code: 'environment.label', default: 'Environment'), environmentInstance.id])}"
        redirect(action: "show", id: environmentInstance.id)
      }
      else {
        render(view: "edit", model: [environmentInstance: environmentInstance])
      }
    }
    else {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'environment.label', default: 'Environment'), params.id])}"
      redirect(action: "list")
    }
  }

  def delete = {
    def environmentInstance = Environment.get(params.id)
    if (environmentInstance) {
      try {
        environmentInstance.delete(flush: true)
        flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'environment.label', default: 'Environment'), params.id])}"
        redirect(action: "list")
      }
      catch (org.springframework.dao.DataIntegrityViolationException e) {
        flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'environment.label', default: 'Environment'), params.id])}"
        redirect(action: "show", id: params.id)
      }
    }
    else {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'environment.label', default: 'Environment'), params.id])}"
      redirect(action: "list")
    }
  }
}
