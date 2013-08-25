import os
import sys
import re
import time
if sys.version_info[0]>2:
  from tkinter import *
  from tkinter import filedialog
  from tkinter import messagebox
  from tkinter import font
  import tkinter as tk
  def debug(elevel, *text):
    exec("print ('[%s]'%elevel, *text)")
else:
  from Tkinter import *
  import Tkinter as tk
  import tkMessageBox as messagebox
  import tkFileDialog as filedialog
  import tkFont as font
  def debug(elevel,*args):
    exec("print '[%s]'%elevel, "+', '.join(args))

# Debug constants
class D:
  I='Info'
  W='Warning'
  E='Error'
  S='Severe'
  # Messages
  class M:
    C='Operation canceled'


sys.stderr.write=lambda l:debug(D.S,l.rstrip())

join=os.path.join

class LinkLabel(Label):
  def __init__(self,*args, **kwargs):
    kwargs['underline']=1
    kwargs['foreground']="blue"
    cmd=kwargs['command']
    del kwargs['command']
    Label.__init__(self, *args, **kwargs)
    self.bind("<Enter>",self.onenter)
    self.bind("<Leave>",self.onleave)
    self.bind("<Button-1>",cmd)
  def onenter(self,e):e.widget.config(cursor="hand2")
  def onleave(self,e):e.widget.config(cursor="")

class INIFile:
  def __init__(self,filename,mode='config'):
    filename=str(filename)
    path=os.path.split(filename)[0]
    if not os.path.exists(path):
      os.makedirs(path)
    if not os.path.exists(filename):
      f=open(filename,'w+')
      f.write('')
    else:
      f=open(filename,'r+')
    self.obj={}
    if mode=='config':
      currentkey=''
      self.obj[currentkey]={}
    self.comments=[]
    i=-1
    for line in f.readlines():
      i+=1
      line=line.rstrip()
      if line.startswith('[') and line.endswith(']') and mode=='config':
        currentkey=line[1:-1]
        self.obj[currentkey]={}
      elif line.lstrip().startswith('#'):
        self.comments.append((line,i))
      else:
        line=line.replace('\t','')
        split=line.split('=')
        if len(split)==1:continue
        key=split[0].strip()
        value='='.join(split[1:]).lstrip()
        if mode=='config':
          self.obj[currentkey][key]=value
        elif mode=='property':
          self.obj[key]=value
    f.close()
    if mode=='config':
      del self.obj['']
    self.filename=filename
    self.mode=mode
  def write(self):
    out=[]
    keys=list(self.obj.keys())
    keys.sort()
    for key in keys:
      if self.mode=='config':
        out.append('[%s]'%key)
        itemkeys=list(self.obj[key].keys())
        itemkeys.sort()
        for itemkey in itemkeys:
          out.append('  %s = %s'%(itemkey,self.obj[key][itemkey]))
      elif self.mode=='property':
        out.append('%s = %s'%(key,self.obj[key]))
    f=open(self.filename,'w')
    for comment in self.comments:
      out.insert(comment[1], comment[0])
    f.write('\n'.join(out))
    f.close()
  def __setitem__(self,key,value):
    if type(value)==dict and self.mode=='config':
      self.obj[key]=value
    elif self.mode=='property':
      self.obj[key]=value
    else:
      raise ValueError('Can only set keys with dict. Use __getitem__ instead')
    self.write()
  def __getitem__(self,key):
    if not key in self.obj:
      self.obj[key]={}
      self.write()
    #dict=self.specialdict(self.obj[key])
    #def update(d):self[key]=d
    #dict.parent(update)
    return self.obj[key]
  def __delitem__(self,key):
    del self.obj[key]
    self.write()
  def merge(self,key,data):
    if not key in self.obj:self.obj[key]={}
    self.obj[key].update(data)
    self.write()
  def haskey(self,key):
    return key in self.obj
  def keys(self):
    return self.obj.keys()


class Function:
  """ Represents a Java function """
  def __init__(self, *args):
    if len(args)<6:
      raise ValueError("Function init not given enough arguments")
    self.list=args
    self.id=args[0]
    self.Jfunction=args[1]
    self.arguments=args[2]
    self.returntype=args[3]
    self.JSfunction=args[4]
    self.changed=args[5]
  def __getitem__(self, i):
    debug(D.W,"[Function] Depreciated call to __getitem__",i)
    return self.list[i]
  def __str__(self):
    return self.returntype+" "+self.Jfunction+"(%s)"%(", ".join([" ".join(arg) for arg in self.arguments]))

class Property:
  """ Represents a java property """
  def __init__(self, *args):
    if len(args)<3:
      raise ValueError("Property init not given enough arguments")
    self.list=args
    self.type=args[0]
    self.name=args[1]
    self.displayname=args[2]
  def __getitem__(self, i):
    debug(D.W, "[Property] Depreciated call to __getitem__",i)
    return self.list[i]


class CCMLGen:
  title="Comcraft ModLoader Javascript Object Generator"
  settingsdir=join(os.getcwd(),"_CCMLG")
  filedlgopts={'filetypes':[('INI Files', '.ini')], 'initialdir':os.getcwd(), 'defaultextension':'.ini'}
  protectedfiles=['BaseMod.java', 'Mod.java', 'ModLoader.java']
  ##
  imports="""
// ModLoader start
import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsObject;
// ModLoader end"""
  extend='JsObject '
  superclass='        super(JsObject.OBJECT_PROTOTYPE); // ModLoader\n'
  evalnative="""    // ModLoader start
    public void evalNative(int id, JsArray stack, int sp, int parCount) {
        switch(id) {
%s
            default:
                super.evalNative(id, stack, sp, parCount);
        }
    }
    // ModLoader end
"""
  funcdefinitions=re.compile('public\s+(static|final|\s*)\s*(\w+)\s+(\w+)\s*\((.*)\)\s*\{')
  propdefs=re.compile('public\s+(static|final|)\s*(final|)\s*([\w\[\]]+)\s+(.*);')
  def class_re(self,f):
    n=os.path.split(f.name)[1][:-len('.java')]
    return (re.compile('public\s+(final\s+|\s*)class\s+'+n),re.compile('(public|protected)\s+%s\s*\(.*\)\s*\{'%n),n)
  cmtexpr=re.compile('[ ]*// ModLoader start.*?ModLoader end[ ]*(\n|$)',re.S)
  addnative=re.compile('(?: ){8}addNative\(".*", (ID_[A-Z_0-9$]+), [0-9]+\);')
  addvar=re.compile('(?: ){8}addVar\("(.*)", (.*)\);')
  ##
  def __init__(self):
    if not os.path.exists(self.settingsdir):
      os.makedirs(self.settingsdir)
    self.ccdir=os.getcwd()
    self.classfiles=[]
    root = Tk()
    root.title(self.title)
    root.resizable(0,0)
    menubar = Menu(root)
    filemenu = Menu(menubar, tearoff=0)
    filemenu.add_command(label="Open Config", command=self.openconfig)
    filemenu.add_command(label="Save Copy Of Config", command=self.saveconfig)
    filemenu.add_separator()
    filemenu.add_command(label="Change Source Directory", command=self.chdir)
    filemenu.add_separator()
    filemenu.add_command(label="Exit", command=root.destroy)
    viewmenu = Menu(menubar, tearoff=0)
    viewmenu.add_command(label="Refresh", command=self.refresh)
    viewmenu.add_separator()
    viewmenu.add_command(label="Todo List", command=self.todo)
    helpmenu = Menu(menubar, tearoff=0)
    helpmenu.add_command(label="How To Use", command=self.howto)
    menubar.add_cascade(label="File", menu=filemenu)
    menubar.add_cascade(label="View", menu=viewmenu)
    menubar.add_cascade(label="Help", menu=helpmenu)
    root.config(menu=menubar)
    self.root=Frame(root, width=800, height=600)
    self.root.pack(fill=BOTH)
    selfiles=Frame(self.root,pady=10, padx=5)
    selfiles.pack(fill=BOTH,side=BOTTOM)
    Label(self.root, text="Not currently JS Objects").pack(side=LEFT)
    Label(self.root, text="Currently JS Objects").pack(side=RIGHT)
    lbframe=Frame(selfiles)
    lbframe.pack(fill=BOTH, side=BOTTOM)
    scrollbar = Scrollbar(lbframe, orient=VERTICAL)
    opts=dict(yscrollcommand=scrollbar.set, selectmode=EXTENDED,height=40,activestyle=NONE)
    lbnot=Listbox(lbframe, **opts)
    lbis=Listbox(lbframe, **opts)
    def dual(*args,**kwargs):
      lbnot.yview(*args,**kwargs)
      lbis.yview(*args,**kwargs)
    scrollbar.config(command=dual)
    scrollbar.pack(side=RIGHT, fill=Y)
    lbnot.pack(side=LEFT)
    lbis.pack(side=RIGHT)
    b1=Button(selfiles, text="Make Object",state=DISABLED, command=self.makeobjects)
    b1.pack(side=LEFT)
    b2=Button(selfiles, text="Remove Object",state=DISABLED, command=self.remobjects)
    b2.pack(side=RIGHT)
    b3=Button(selfiles, text="Edit Object",state=DISABLED, command=self.editobjects)
    b3.pack(side=RIGHT)
    def enablebtn(e=None):
      if lbnot.curselection():b1.config(state=NORMAL)
      else:b1.config(state=DISABLED)
      if lbis.curselection():b2.config(state=NORMAL);b3.config(state=NORMAL)
      else:b2.config(state=DISABLED);b3.config(state=DISABLED)
    lbnot.bind("<<ListboxSelect>>", enablebtn)
    lbis.bind("<<ListboxSelect>>", enablebtn)
    def chk(e=None):
      for i in self.disabled:
        if lbnot.selection_includes(i):
          lbnot.selection_clear(i)
          enablebtn()
    def sel1(e):
      lbnot.selection_set(0,END)
      enablebtn()
      chk()
    def sel2(e):
      lbis.selection_set(0,END)
      enablebtn()
    selall1=LinkLabel(selfiles, text="Select All",command=sel1)
    selall1.pack(side=LEFT)
    selall2=LinkLabel(selfiles, text="Select All",command=sel2)
    selall2.pack(side=RIGHT)
    lbnot.bind("<<ListboxSelect>>", chk, add="+")
    self.notdonelist=lbnot
    self.donelist=lbis
    self.listscroll=scrollbar
    self.openconfig(join(self.settingsdir,'config.ini'))
    root.mainloop()
  def openconfig(self,filename=None):
    if not filename:
      filename=filedialog.askopenfilename(**self.filedlgopts)
    if not filename:return
    self.config=INIFile(filename)
    self.loadfiles()
    self.guiupdate()
  def chdir(self):
    self.askroot()
    self.loadfiles()
    self.guiupdate()
  def refresh (self):
    self.openconfig(self.config.filename)
  def saveconfig(self):
    d=filedialog.asksaveasfilename(**self.filedlgopts)
    if d:
      f=INIFile(d)
      f.obj=self.config.obj
      f.write()
  def loadfiles(self):
    settings=self.config['settings']
    if not 'runonce' in settings:settings['runonce']='True'
    if not 'comcraft_root' in settings and settings['runonce']=='False':
      self.askroot()
    elif settings['runonce']=='True':
      self.startup()
      self.loadfiles()
      return
    root=settings['comcraft_root']
    self.ccdir=root
    self.classfiles=[]
    for file in os.listdir(join(root, 'net/comcraft/src')):
      if file.endswith('.java') and file not in self.protectedfiles:
        self.classfiles.append(file)
  def askroot(self):
    dir=filedialog.askdirectory(title="Select Comcraft Source Directory",mustexist=True, initialdir=self.ccdir)
    if dir and os.path.exists(os.path.join(dir,"net\\comcraft\\src")):
      self.config.merge('settings',{'comcraft_root':dir.replace('/','\\')})
    else:
      if messagebox.askokcancel("Not found","Could not locate source, reselect folder?"):
        self.askroot()
  def guiupdate(self):
    self.donelist.delete(0,END)
    self.notdonelist.delete(0,END)
    self.disabled=[]
    longest1=32
    longest2=32
    for file in self.classfiles:
      if file in self.config['done']:
        if len(file)>longest1:longest1=len(file)
        self.donelist.insert(END, file)
      else:
        if len(file)>longest2:longest2=len(file)
        self.notdonelist.insert(END, file)
        if file in self.config['no_extend']:
          self.notdonelist.itemconfig(END, fg="#D00", bg="SystemDisabledText")
          self.disabled.append(self.notdonelist.size()-1)
    self.donelist.config(width=longest1)
    self.notdonelist.config(width=longest2)

  def openfile(self,filename):
    return open(join(self.config['settings']['comcraft_root'],'net\\comcraft\\src\\',filename),'r+',encoding='UTF-8')

  def makeobjects(self):
    for i in self.notdonelist.curselection():
      name=self.notdonelist.get(i)
      f=self.openfile(name)
      debug(D.I, 'Make object',name)
      if self.makeobject(f):
        self.config['done'][name]='True'
        self.config.write()
        debug(D.I,'object creation complete')
      else:
        debug(D.I, D.M.C)
      f.close()
      self.guiupdate()
  def remobjects(self):
    for i in self.donelist.curselection():
      name=self.donelist.get(i)
      f=self.openfile(name)
      self.remobject(f)
      f.close()
      del self.config['done'][name]
    self.config.write()
    self.guiupdate()

  def matchfunction(self, strp, functions):
    m=self.funcdefinitions.match(strp)
    if m:
      args=[re.split('\s+',a) for a in re.split('\s*,\s*',m.group(4))]
      if not args[0][0]:args=[]
      # regex from http://stackoverflow.com/questions/5020906/
      # I tryed this myself but didn't fully work
      idname='ID_'+re.sub(r'((?<=[a-z])[A-Z]|(?<!\A)[A-Z](?=[a-z]))',r'_\1',m.group(3)).upper()
      jsname=m.group(3)
      mod_flag=0
      # Make sure every id is unique {
      ids=[func.id for func in functions];id=idname;i=1
      while id in ids:id=idname+"$"+str(i);i+=1
      idname=id;jsname=jsname+str(i-1) if i>1 else jsname
      if i>1:mod_flag=True
        # }
      return Function(idname,m.group(3),args,m.group(2), jsname, mod_flag)
  def matchproperty(self, strp):
    m=self.propdefs.match(strp)
    o=[]
    if m:
      s=re.split('\s*=\s*', m.group(4))
      g=[]
      for string in m.groups():
        if string:g.append(string)
      for name in re.split('\s*,\s*',s[0]):
        o.append (Property(g[-2],name,' '.join(g)))
      return o


  def makeobject(self, f,lines=None, selections=[[],[]]):
    inittime=time.time()
    classdef,classinit,filename=self.class_re(f)
    functions=[]
    properties=[]
    buffer=[]
    done=[]
    indexes={}
    comment=False
    addsuper=0
    if lines is None:lines=f
    for line in lines:
      buffer.append(line)
      strp=line.strip()
      ## < ignore comments
      if comment and not strp.startswith('*/'):continue
      else:comment=False
      if strp.startswith('/*'):
        comment=True
        continue
      if strp.startswith('//'):continue
      ## >
      # size of the current line indent
      indent=len(line)-len(line.lstrip())
      if line.startswith('package'):
        buffer[-1]+=self.imports
        done.append('imports')
        continue
      if classdef.match(strp) and 'extend' not in done:
        i=line.index('{')
        if line.find('extends')>-1:
          messagebox.showerror("Error", "Cannot extend class "+filename+", there is already a superclass.")
          self.config.merge('no_extend', {filename+'.java': 'True'})
          return 0
        buffer[-1]=line[:i-1]+' extends '+self.extend+line[i:-1]+' // ModLoader\n'
        done.append('extend')
        continue
      m=classinit.match(strp)
      if m:
        if not 'init' in indexes:indexes['init']=[]
        indexes['init'].append((len(buffer),strp[m.start():m.end()-1]))

      fn=self.matchfunction(strp,functions)
      if fn:
        functions.append(fn)
        continue
      pr=self.matchproperty(strp)
      if pr:
        for e in pr:
          properties.append(e)
        continue
    debug(D.I,'Parsed class',filename,'in',time.time()-inittime,'Seconds')
    if not 'init' in indexes:
      messagebox.showerror('No Initializer','Class '+filename+' has no initializer')
      return 0
    if len(indexes['init'])==1:
      buffer[indexes['init'][0][0]-1]+=self.superclass
      indexes['init']=indexes['init'][0][0]
    else:
      window=self.makewindow("Select Initiator")
      v=IntVar()
      Label(window, text="Multiple class initiators found for class "+filename).pack()
      li=Listbox(window)
      w=20
      li.pack()
      for init in indexes['init']:
        if (len(init[1])/4)*3>w:w=(len(init[1])/4)*3
        li.insert(END, init[1])
      li.config(width=int(w+0.5),height=len(init))
      def use():
        v.set(1)
        pos=indexes['init'][int(li.curselection()[0])][0]
        buffer[pos-1]+=self.superclass
        window.destroy()
        indexes['init']=pos
      b=Button(window, text="Use Initiator",command=use,state=DISABLED)
      b.pack()
      li.bind('<<ListboxSelect>>',lambda e:b.config(state=NORMAL))
      self.windowrun(window)
      if not v.get():return 0
    onfinish=lambda fn,pr:self.writeobject(f,indexes,buffer,fn,pr)
    success=self.askforselections(functions, properties, onfinish,selections, filename)
    return success
  def askforselections(self,functions, properties, callback, selected=[[],[]], name="(Unknown)"):
    root=self.makewindow("Attributes for Class "+name)
    head=Label(root)
    head.pack()
    try:
      fnt=font.nametofont("TkDefaultFont").copy()
    except TclError:
      fnt=font.Font(font=("Courier", 12, "normal"))
    fnt.configure(weight='bold')
    fnt.configure(size=11)
    head.config(text="Please select the FUNCTIONS\navailable in Javascript", font=fnt)
    frame=Frame(root)
    frame.pack()
    scroll=Scrollbar(frame)
    scroll.pack(side=RIGHT, fill=Y)
    lb=Listbox(frame,yscrollcommand=scroll.set,selectmode=EXTENDED, height=25)
    lb.pack(fill=BOTH, expand=True)
    scroll.config(command=lb.yview)
    lbwidth=30
    for function in functions:
      text=str(function)
      if len(text)>lbwidth:lbwidth=len(text)
      lb.insert(END, text)
    for idx in selected[0]:lb.selection_set(idx)
    lb.config(width=lbwidth)
    self.zeroflag=0
    tempscope=[functions, properties]
    def complete(e=None):
      opr=tempscope[1]
      npr=[]
      for i in map(int, lb.curselection()):
        npr.append(opr[i])
      tempscope[1]=npr
      root.destroy()
      callback(*tempscope)
      self.zeroflag=1
    def contin(e=None):
      button.config(text="Create object", command=complete)
      lbwidth=lb.config()['width'][-1]
      ofn=tempscope[0]
      nfn=[]
      for i in map(int, lb.curselection()):
        nfn.append(ofn[i])
      tempscope[0]=nfn
      lb.delete(0,END)
      for property in properties:
        text=property.displayname
        if len(text)>lbwidth:lbwidth=len(text)
        lb.insert(tk.END, text)
      for idx in selected[1]:lb.selection_set(idx)
      lb.config(width=lbwidth)
      head.config(text=head['text'].replace('FUNCTIONS','ATTRIBUTES'))
      def selectbytype():
        sels=list(map(int,lb.curselection()))
        root = self.makewindow("Select types")
        listb=Listbox(root, selectmode=MULTIPLE,width=30,heigh=15)
        types={}
        i=0
        for pr in properties:
          if pr.type not in types:
            types[pr.type]=[]
            listb.insert(END, pr.type)
          types[pr.type].append(i)
          i+=1
        listb.pack()
        var=IntVar()
        def go():
          for x in map(int,listb.curselection()):
            for i in types[listb.get(x)]:
              lb.selection_set(i)
          if not var.get():
            for i in sels:lb.selection_set(i)
          root.destroy()
        Checkbutton(root, text="Clear previous selections",variable=var).pack(side=LEFT)
        Button(root, text="Select", command=go).pack(side=BOTTOM)
        self.windowrun(root)
      selmenu.entryconfig(END, label="By type", command=selectbytype)
    def selectbyreturn():
      sels=list(map(int,lb.curselection()))
      root = self.makewindow("Select return types")
      listb=Listbox(root, selectmode=MULTIPLE,width=30,heigh=15)
      types={}
      i=0
      for fn in functions:
        if fn.returntype not in types:
          types[fn.returntype]=[]
          listb.insert(END, fn.returntype)
        types[fn.returntype].append(i)
        i+=1
      listb.pack()
      var=IntVar()
      def go():
        for x in map(int,listb.curselection()):
          for i in types[listb.get(x)]:
            lb.selection_set(i)
        if not var.get():
          for i in sels:lb.selection_set(i)
        root.destroy()
      Button(root, text="Select", command=go).pack(side=BOTTOM)
      Checkbutton(root, text="Clear previous selections",variable=var).pack(side=LEFT)
      self.windowrun(root)
    selmenu = Menu(root, tearoff=0)
    selmenu.add_command(label="All", command=lambda:lb.selection_set(0,END))
    selmenu.add_command(label="None", command=lambda:lb.selection_clear(0,END))
    selmenu.add_command(label="By return type", command=selectbyreturn)
    def select(e):
      try:
        selmenu.tk_popup(e.x_root, e.y_root, 0)
      finally:
        selmenu.grab_release()

    LinkLabel(root, text="Select...", command=select).pack(side=LEFT, padx=lbwidth)
    button=Button(root, text="Continue",command=contin)
    button.pack(side=LEFT)
    self.windowrun(root)
    return self.zeroflag

  def writeobject(self, f, indexes, buffer,functions, properties):
    renames=[]
    staticids='    // ModLoader start\n'
    i=100
    idx=indexes['init']-2
    while not buffer[idx].strip():idx-=1
    for fn in functions:
      staticids+='    private static final int %s = %i;\n'%(fn.id,i)
      i+=1
    buffer[idx]+=staticids+'    // ModLoader end\n'

    addattrs=''
    idx=indexes['init']
    while not buffer[idx].startswith( '    }'):idx+=1
    idnt=' '*8
    addattrs+=idnt+'// Methods\n'
    for fn in functions:
      addattrs+=idnt+'addNative("%s", %s, %i);\n'%(fn.JSfunction,fn.id,len(fn.arguments))
      if fn.changed:renames.append(fn)
    addattrs+=idnt+'// Properties\n'
    for prop in properties:
      val=prop.name
      # these types are not derived from the Object class or need modifying
      for before,after in {
        'int':'new Integer(%s)',
        'long':'new Long(%s)',
        'float':'new Float(%s)',
      }.items():
        if prop.type.endswith(before):val=after%val
      if prop.type.endswith('[]'):
        addattrs+=idnt+'JsArray arr_%s= new JsArray();for(int i=0;i<%s.length;i++){arr_%s.setObject(i,%s[i]);}\n'%(prop.name, prop.name, prop.name, prop.name)
        val = 'arr_'+val
      addattrs+=idnt+'addVar("%s", %s);\n'%(prop.name,val)
    buffer[idx]=idnt+'// ModLoader start\n'+addattrs+idnt+'// ModLoader end\n'+buffer[idx]

    ind=0
    endclass=0
    while 1:
      ind-=1
      l=buffer[ind].rstrip()
      if l.endswith('}'):
        if endclass==0:endclass=1
        else:break
    switches=''
    stackset=['Int','Object','Boolean']
    for fn in functions:
      action=''
      rtype=fn.returntype.capitalize()
      if rtype not in stackset and rtype!='Void':
        if rtype=='Float':
          rtype='Number'
          fn.Jfunction='(double) '+fn.Jfunction
        elif rtype=='Double':
          rtype='Number'
        else:
          rtype='Object'
      args=self.makeArgsStack(fn.arguments)
      if rtype!='Void':
        action='stack.set%s(sp,%s(%s));'%(rtype,fn.Jfunction,args)
      else:
        action='%s(%s);'%(fn.Jfunction,args)
      switches+='            case '+fn.id+':\n'
      switches+='                '+action+'\n'
      switches+='                '+'break;\n'
    s=self.evalnative%(switches)
    if 'evalnative' not in indexes:
      buffer[ind]+=s
    f.seek(0)
    f.truncate()
    f.write(''.join(buffer))
    if renames:
      t="The following functions have been renamed:\n"
      for function in renames:t+='\n'+str(function)+" to "+function.JSfunction
      messagebox.showwarning("Functions renamed", t+"\n\nBecause javascript cannot have multiple function definitions")
    self.registerGlobal(buffer[indexes['init']-1])

  def makeArgsStack(self,arguments):
    args=[]
    for i in range(len(arguments)):
      a=arguments[i]
      cast=''
      t=a[0].capitalize()
      if t not in ['Int','Object','Boolean','String']:
        cast='(%s) '%a[0]
        t='Object'
        if a[0]=='float':t='Number'
      args.append('%sstack.get%s(sp+%i)'%(cast,t,i+2))
    return ', '.join(args)

  def remobject(self, f,lines=None):
    classdef,unused, classname=self.class_re(f)
    debug(D.I, 'Remove class',classname)
    out=[]
    block=False
    if lines is None:lines=f
    for line in lines:
      found=line.find('// ModLoader')
      if found==-1 and not block:
        out.append(line)
      elif classdef.match(line) and not block:
        p=line.find(self.extend)
        if p==-1:
          debug(D.W,"Could not find 'extends",self.extend,"'")
        else:
          b4=line[:p]
          if b4.endswith(','):b4=b4[:-1]
          else:b4=b4[:-len(" extends")]
          line=b4+line[p+len(self.extend):found-1]+line[-1]
        out.append(line)
      elif line[found+13:].startswith('start'):
        block=True
      elif line[found+13:].startswith('end'):
        block=False
    if isinstance(lines, list):
      return out
    else:
      f.seek(0)
      f.truncate()
      f.write(''.join(out))

  def makewindow(self, title=""):
    root=Toplevel(tk._default_root)
    root.resizable(0,0)
    root.title(title)
    root.protocol("WM_DELETE_WINDOW", root.destroy) # onclose
    return root
  def windowrun(self, root):
    root.grab_set() # sets focus
    root.wait_window(root) # holds thread

  def editobjects(self):
    for i in self.donelist.curselection():
      name=self.donelist.get(i)
      self.editobject(self.openfile(name))
  def editobject(self,f):
    allfn=[]
    allpr=[]
    current=[]
    lines=f.readlines()
    for line in lines:
      strp=line.lstrip()
      m=self.matchfunction(strp, allfn)
      if m:allfn.append(m);continue
      m=self.matchproperty(strp)
      if m:allpr.extend(m);continue
      m=self.addnative.match(line)
      if m:current.append(m.groups());continue
      m=self.addvar.match(line)
      if m:current.append(m.groups());continue
    a1=[e.id for e in allfn]
    a2=[e.name for e in allpr]
    sel=[[],[]]
    for c in current:
      if c[0] in a1+a2:
        try:sel[0].append(a1.index(c[0]))
        except ValueError:sel[1].append(a2.index(c[0]))
    debug(D.I, 'Edit object',os.path.split(f.name)[1])
    if self.makeobject(f,self.remobject(f,lines),sel):
      debug(D.I,'object edit complete')
    else:
      debug(D.I, D.M.C)
    f.close()

  def registerGlobal(self, initline):
    line=initline.splitlines()[0]
    m=re.match('\s*(?:public|protected)\s+(\w+)\s*\((.*)\)\s*\{',line)
    if not m:
      debug(D.W, 'Cannot find match to register object')
      return
    name=m.group(1)
    args=[re.split('\s+',a) for a in re.split('\s*,\s*',m.group(2))]
    f=self.openfile('BaseMod.java')
    info=f.readline()[len('//CCMLGen: '):].split(',')
    info[0]=list(map(int,info[0].split('.')))
    info[1]=int(info[1]);info[2]=int(info[2])
    i=1
    b=[]
    for line in f:
      i+=1
      if i == info[0][0]:
        line+='    private static final int ID_%s = %d;\n'%(name.upper(), info[0][1]+1)
      elif i == info[1]:
        line+='        addVar("%s", new JsFunction(ID_%s, %d));\n'%(name, name.upper(), len(args))
      elif i == info[2]:
        line+='        case ID_%s:\n            stack.setObject(sp, new %s(%s));\n            break;\n'%(name.upper(),name,self.makeArgsStack(args))
      b.append(line)
    b.insert(0,'//CCMLGen: %d.%d,%d,%d\n'%(info[0][0]+1,info[0][1]+1,info[1]+2,info[2]+5))
    f.seek(0)
    f.truncate()
    f.write(''.join(b))
    f.close()

  def howto(self):
    helptext="""
Welcome to the Comcraft ModLoader Generator!

This utility enables a user to convert a Java class in the Comcraft source to a Javascript object
made by MiniJoe - a "Minimal Javascript Object Environment" Homepage: code.google.com/p/minijoe/
Comcraft is a game inspired by minecraft for J2ME devices. Homepage: www.comcraft-game.blogspot.com/
ModLoader is a Mod for Comcraft Made by simon816, (who also made this utility). Homepage:
github.com/simon816/ComcraftModLoader.

The Main Screen
  Menubar
    File->
      Open Config - Select a config file
      Save Copy Of Config - Choose a location to save the current setup
      Change Source Directory - Reselect the location of comcraft source files
      Exit - Quits this utility
    View->
      Refresh - rescans files and reloads config
    Help->
      How To Use - Opens this help window

  The rest of the screen is divided into two sections, on the left is class files that have not yet
  been converted to javascript objects and on the right are classes that have.
  The scrollbar has dual operation of both class lists, select all will select all class files in the
  list.
  All lists are multi-select meaning you can drag a selection, use ctrl+click and shift+click like
  other applications.
  The Make Object button opens the make object screen (see below) for every file currently in the selection.
  The Edit Object opens the make object screen (again, see below) but is used to change previously
  generated objects.
  Remove Objects button will simply reset the class files to how they were before turned into a javascript
  object.

The Make Object Screen
  A popup window appears (locking the main screen) with firstly a list of only the public methods in
  javascript presented in a format that looks like it would in its source code. Again, the list is multi-select.
  On clicking Continue at the bottom, the list updates with a list of public properties for the class. Then
  click Create Object to make the javascript object, the main screen will update. If the window closes before
  this point, no changes will take place. It is worth noting the title of the window tells you what class is
  being accessed, this becomes usefull when selecting multiple class files.

    """
    root=self.makewindow("CCMLGen Help")
    Label(root, text=helptext, bg="white", justify=LEFT, padx=8).pack()
    self.windowrun(root)

  def startup(self):
    self.config.merge('settings', {'runonce':'False'})
    introtext="""Welcome to Comcraft ModLoader Javascript Generator
This is first run, when you close this dialog, review the
help (menubar Help->How To Use) or select the comcraft source
directory (File-> Change Source Directory)
"""
    messagebox.showinfo("First Run", introtext)
  def todo(self):
    todo=[
      'Fix \'Edit Object\' registering in BaseMod',
      'Detect initiator used when editing an existing object',
      'Unregister objects when removing them'
      ]
    messagebox.showinfo("Todo list", '\n'.join([('[%d] '%(todo.index(itm)+1))+itm for itm in todo]))
if __name__=='__main__':
  app=CCMLGen()
