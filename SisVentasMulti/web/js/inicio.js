const deleteTags = (node) => {
  /*
   *  Se eliminan todas las etiquetas que contengan z-label
   *    en el nodo padre pasado por argumento
   */

  const container = document.querySelector(node);

  const list = Array.from(container.querySelectorAll(".z-label"));
  list.forEach((e) => e.classList.remove("z-label"));
};

const openLogin = () => {
  const login = document.querySelector(".btnLogin");
  const modal = document.querySelector(".login_wrapper");
  const loginContainer = document.querySelector(".login_container_id");
  const forgot = document.querySelector(".forgot");

  const openModal = () => {
    loginContainer.style.display = "flex";
  };

  const closeModal = (e) => {
    if (!modal.contains(e.target) && e.target !== login) {
      loginContainer.style.display = "none";
    }
  };
  login.addEventListener("click", openModal);
  document.addEventListener("click", (e) => closeModal(e));
  forgot.addEventListener("click", () => {
    loginContainer.style.display = "none";
  });
};

const resolvePasswordInput = () => {
  if (typeof zk !== "undefined" && zk.Widget) {
    const widget = zk.Widget.$("$password");
    if (widget) {
      if (typeof widget.getInputNode === "function") {
        const node = widget.getInputNode();
        if (node) {
          return node;
        }
      }
      const root = typeof widget.$n === "function" ? widget.$n() : null;
      if (root) {
        if (root.tagName === "INPUT") {
          return root;
        }
        const nested = root.querySelector("input");
        if (nested) {
          return nested;
        }
      }
    }
  }

  const wrap = document.querySelector(".input_password");
  if (wrap) {
    if (wrap.tagName === "INPUT") {
      return wrap;
    }
    const nested = wrap.querySelector("input");
    if (nested) {
      return nested;
    }
  }

  return document.querySelector(".wrapper_input input[type='password']");
};

const eyeSvgVisible =
  '<svg class="icon_eye_svg" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8S1 12 1 12z"></path><circle cx="12" cy="12" r="3"></circle></svg>';

const eyeSvgHidden =
  '<svg class="icon_eye_svg" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M17.94 17.94A10.94 10.94 0 0 1 12 20c-7 0-11-8-11-8a21.8 21.8 0 0 1 5.06-6.94"></path><path d="M9.9 4.24A10.94 10.94 0 0 1 12 4c7 0 11 8 11 8a21.83 21.83 0 0 1-2.16 3.19"></path><path d="M14.12 14.12a3 3 0 1 1-4.24-4.24"></path><line x1="1" y1="1" x2="23" y2="23"></line></svg>';

const showPassword = () => {
  const field = resolvePasswordInput();
  const icon = document.querySelector(".icon_eye");
  if (!field || !icon) {
    return;
  }

  if (icon.dataset.toggleBound === "1") {
    return;
  }
  icon.dataset.toggleBound = "1";

  const toggleShowPass = (event) => {
    event.preventDefault();
    event.stopPropagation();
    const hidden = field.getAttribute("type") === "password";
    field.setAttribute("type", hidden ? "text" : "password");
    icon.innerHTML = hidden ? eyeSvgHidden : eyeSvgVisible;
    icon.setAttribute("title", hidden ? "Ocultar contraseña" : "Mostrar contraseña");
    icon.setAttribute("aria-label", hidden ? "Ocultar contraseña" : "Mostrar contraseña");
  };

  icon.addEventListener("click", toggleShowPass);
};

const stopLoading = () => {
  const loading = document.querySelector(".loading_inicio");
  loading.style.display = "none";
};

(() => {
  zk.afterMount(function () {
    showPassword();
    setTimeout(showPassword, 300);
    setTimeout(() => {
      deleteTags(".login_container"); //inicio.zul
      showPassword();
      stopLoading();
    }, 2000);
  });
})();
